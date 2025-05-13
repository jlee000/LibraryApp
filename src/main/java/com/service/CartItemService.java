package com.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exception.IventoryDepletedException;
import com.exception.ResourceNotFoundException;
import com.model.Book;
import com.model.Cart;
import com.model.Cartitem;
import com.repository.CartItemRepo;
import com.repository.CartRepo;

@Service
public class CartItemService {
    
    @Autowired
    private CartItemRepo cartItemRepo;

    @Autowired
	private CartRepo cartRepo;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private BookService bookService;
	
	
	public void addCartItem(Long cartId, Long bookId, int quantity) {
		Cart cart = cartService.getCartByCartId(cartId);
		Book book = bookService.getBookById(bookId);
		Cartitem cartItem = cart.getCartItems()
			.stream()
			.filter(item-> item.getBook().getId().equals(bookId))
			.findFirst().orElse(new Cartitem());
		
		if(cartItem.getId() == null) {
			cartItem.setCart(cart);
			cartItem.setBook(book);
			cartItem.setQuantity(quantity);            
		}else {
			cartItem.setQuantity(cartItem.getQuantity()+quantity);
		}
		
		if(bookService.checkStock(book,quantity)){
			book.setStock(book.getStock()-quantity); 
			bookService.updateBook(book);
			cart.addItem(cartItem);
			cartItemRepo.save(cartItem);
			cartRepo.save(cart);
		}else { throw new IventoryDepletedException("Not enough stock"); }	
	}
	
	public void removeCartItem(Long cartId, Long bookId) {
		Cart cart = cartService.getCartByCartId(cartId);
		Cartitem itemToRemove = getCartItem(cartId, bookId);
		cart.removeItem(itemToRemove);
		cartRepo.save(cart);
		
		Book book = bookService.getBookById(bookId);
		book.setStock(book.getStock() + itemToRemove.getQuantity()); 
		bookService.updateBook(book);
	}
	
	public void updateCartItemQuantity(Long cartId, Long bookId, int quantity){	
		Cart cart = cartService.getCartByCartId(cartId);
		cart.getCartItems().stream().filter(item->item.getBook().getId().equals(bookId))
		.findFirst()
		.ifPresent(item-> {
			Book book = bookService.getBookById(bookId);
			if(quantity > item.getQuantity()) {
				book.setStock(book.getStock()-(quantity-item.getQuantity())); 
			}else {
				book.setStock(book.getStock()+
									(item.getQuantity()-quantity));
			}bookService.updateBook(book);			
			item.setQuantity(quantity);
			cartRepo.save(cart);
			});
	}
	
	public Cartitem getCartItem(Long cartId, Long bookId) {
		Cart cart = cartService.getCartByCartId(cartId);
		return cart.getCartItems()
		.stream()
		.filter(item -> item.getBook().getId().equals(bookId))
		.findFirst().orElseThrow(()-> new ResourceNotFoundException("BookId not found") );
		
	}
    
}
