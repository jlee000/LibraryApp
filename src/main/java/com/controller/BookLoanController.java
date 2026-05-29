package com.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.config.ApiResponse;
import com.exception.ResourceNotFoundException;
import com.model.Book;
import com.model.BookLoan;
import com.model.BookLoanItem;
import com.service.BookLoanService;
import com.service.BookService;

@RestController
public class BookLoanController {
    
    @Autowired
    private BookLoanService bookLoanService;

	@Autowired
	private BookService bookService;

	@PostMapping("loan/{userId}/{daysToLoan}")
	public ResponseEntity<ApiResponse> createLoan(@PathVariable Long userId, @PathVariable int daysToLoan){
		try {
		BookLoan loan = bookLoanService.createLoan(userId,daysToLoan);
		return ResponseEntity.ok(new ApiResponse("Create loan success",loan));
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Create loan failed",e.getMessage()));
		}		
	}

	@PutMapping("loan/return/{loanId}")
	public ResponseEntity<ApiResponse> returnBooks(@PathVariable Long loanId){
		try {
			BookLoan loan = bookLoanService.returnBooks(loanId);
			return ResponseEntity.ok(new ApiResponse("Success",loan));	
		}catch(ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed",e.getMessage()));
		}	
	}
	
	@GetMapping("loan/{loanId}")
	public ResponseEntity<ApiResponse> getBookLoanById(@PathVariable Long loanId){
		try {
		BookLoan loan = bookLoanService.getBookLoan(loanId);
		return ResponseEntity.ok(new ApiResponse("Success",loan));	
		}catch(ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed",e.getMessage()));
		}		
	}
	
	@GetMapping("loan/user/{userId}")
	public ResponseEntity<ApiResponse> getUserBookLoans(@PathVariable Long userId){
		try {
		List<BookLoan> bookLoans = bookLoanService.getUserBookLoans(userId);
		return ResponseEntity.ok(new ApiResponse("Success",bookLoans));	
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed",e.getMessage()));
		}		
	}

	@GetMapping("loanitems/{loanId}")
	public ResponseEntity<ApiResponse> getBooksFromLoanItems(@PathVariable Long loanId){
		try {
			List<BookLoan> bookLoans = bookLoanService.getUserBookLoans(loanId);
			List<BookLoanItem> bookLoanItems = new ArrayList<>();
			for( BookLoan loan: bookLoans){
				for (BookLoanItem loanItem : loan.getBookLoanItems()) {
					bookLoanItems.add(loanItem);
				}				
			}		
			List<Book> books = new ArrayList<>();
			for(BookLoanItem loanItem : bookLoanItems){
				books.add(bookService.getBookById(loanItem.getBook().getId()));
			}
		return ResponseEntity.ok(new ApiResponse("Success",books));	
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed",e.getMessage()));
		}		
	}
    
}
