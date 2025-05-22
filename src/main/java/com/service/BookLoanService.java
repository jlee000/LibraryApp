package com.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.config.LoanStatus;
import com.exception.ResourceNotFoundException;
import com.model.Book;
import com.model.BookLoan;
import com.model.BookLoanItem;
import com.model.Cart;
import com.repository.BookLoanRepo;
import com.repository.BookRepo;

@Service
public class BookLoanService {
    
    @Autowired
    private BookLoanRepo bookLoanRepo;

	@Autowired
	private BookRepo bookRepo;    
	
	@Autowired
	private CartService cartService;

	@Autowired
	private BookService bookService;

	
	public BookLoan getBookLoan(Long loanId) {
		return bookLoanRepo.findById(loanId).orElseThrow(()->new ResourceNotFoundException("LoanId not found"));
	}
	
	public List<BookLoan> getUserBookLoans(Long userId) {
		return bookLoanRepo.findByUserId(userId);
	}
	
	public BookLoan createLoan (Long userId, int daysToLoan) {
		Cart cart = cartService.getCartByUserId(userId);
		BookLoan loan = createBookLoan(cart,daysToLoan);
		BookLoan savedLoan = bookLoanRepo.save(loan);
		cartService.clearCart(cart.getId());
		return savedLoan;
	}
	
	private BookLoan createBookLoan(Cart cart, int daysToLoan) {
		BookLoan bl = new BookLoan();
		bl.setUser(cart.getUser());
		bl.setLoanStatus(LoanStatus.LOAN);	
		bl.setDaysToLoan(daysToLoan);
		bl.setDueDate(bl.getLoanDate().plusDays(daysToLoan));
		List<BookLoanItem> bookLoanItems = new ArrayList<>();
		cart.getCartItems().forEach(cartItem -> {
			Book b = cartItem.getBook();
			b.setStock(b.getStock()-cartItem.getQuantity());
			bookRepo.save(b);

			BookLoanItem newLoanItem = new BookLoanItem(cartItem.getQuantity(), bl, b);
			newLoanItem.setLoanItemValue();	
			bookLoanItems.add(newLoanItem);	}
		);
		bl.setBookLoanItems(bookLoanItems);
		bl.setTotalLoanValue();		
		return bl;
	}

    public BookLoan returnBooks(Long loanId) {
		try {
			BookLoan loan = bookLoanRepo.findById(loanId).orElseThrow(()-> new ResourceNotFoundException("LoanId not found"));
			loan.setLoanStatus(LoanStatus.RETURNED);
			loan.setReturnDate(LocalDateTime.now());

			loan.getBookLoanItems().forEach(bookLoanItem -> {
				Book b = bookService.getBookById(bookLoanItem.getBook().getId());
				b.setStock(b.getStock() + bookLoanItem.getQuantity()); 
				bookService.updateBook(b);
			});

			return bookLoanRepo.save(loan);
		}catch(ResourceNotFoundException e) {
			throw new RuntimeException(e.getMessage());
		}
    }  
}
