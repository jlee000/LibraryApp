package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.config.ApiResponse;
import com.exception.ResourceNotFoundException;
import com.model.BookLoan;
import com.service.BookLoanService;

@RestController
public class BookLoanController {
    
    @Autowired
    private BookLoanService bookLoanService;

	@PostMapping("loan")
	public ResponseEntity<ApiResponse> createLoan(@RequestParam Long userId, @RequestParam int daysToLoan){
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

    
}
