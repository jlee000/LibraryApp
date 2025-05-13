package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.config.ApiResponse;
import com.exception.AlreadyExistsException;
import com.model.Book;
import com.service.BookService;

@RestController
public class BookController {
    
    @Autowired
    private BookService bookService;

	@GetMapping("book")
	public ResponseEntity<ApiResponse> getBooks(){
		List<Book> books = bookService.getBooks();
		if(books.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Failed",books));	
		}
		return ResponseEntity.ok(new ApiResponse("Success",books));
	}	
	
	@GetMapping("book/{bookId}")
	public ResponseEntity<ApiResponse> getBookById(@PathVariable Long bookId){
		Book book = bookService.getBookById(bookId);	
		if(book==null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Failed",book));	
		}
		return ResponseEntity.ok(new ApiResponse("Success",book));
	}
	
	@GetMapping("book/counttitleandauthor")
	public ResponseEntity<ApiResponse> countBooksByTitleAndAuthorName(@RequestParam String title,@RequestParam String name){
		Long count = bookService.countBooksByTitleAndAuthorName(title,name);	
		return ResponseEntity.ok(new ApiResponse("Success",count));
	}

    @GetMapping("book/counttitle")
	public ResponseEntity<ApiResponse> countBooksByTitle(@RequestParam String title){
		Long count = bookService.countBooksByTitle(title);	
		return ResponseEntity.ok(new ApiResponse("Success",count));
	}

    @GetMapping("book/countauthor")
	public ResponseEntity<ApiResponse> countBooksByAuthorName(@RequestParam String name){
		Long count = bookService.countBooksByAuthorName(name);	
		return ResponseEntity.ok(new ApiResponse("Success",count));
	}
	
	@GetMapping("book/title")
	public ResponseEntity<ApiResponse> getBooksByTitle(@RequestParam String title){
		List<Book> books = bookService.getBooksByTitle(title);		
		if(books.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Failed",books));	
		}
		return ResponseEntity.ok(new ApiResponse("Success",books));
	}
	
	@GetMapping("book/author")
	public ResponseEntity<ApiResponse> getBooksByAuthorName(@RequestParam String name){
		List<Book> books = bookService.getBooksByAuthorName(name);
		if(books.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Failed",books));	
		}
		return ResponseEntity.ok(new ApiResponse("Success",books));
	}
	
	@PostMapping("book")
	public ResponseEntity<ApiResponse> addBook(@RequestBody Book b){
		try {
			bookService.addBook(b);
			return ResponseEntity.ok(new ApiResponse("Add success",b));
		}catch(AlreadyExistsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse("Add failed",e.getMessage()));
		}
	}
		
	@PostMapping("books")
	public ResponseEntity<ApiResponse> addBooks(@RequestBody List<Book> books){
		try {
			for(Book b: books) {
				bookService.addBook(b);
			}
			return ResponseEntity.ok(new ApiResponse("Add success",books));

		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Add failed",e.getMessage()));
		}
	}
	
	@PutMapping("book")
	public ResponseEntity<ApiResponse> updateBook(@RequestBody Book b){
		try {		
			bookService.updateBook(b);
			return ResponseEntity.ok(new ApiResponse("Update success",b));
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Update failed",e.getMessage()));
		}
	}
	
	@DeleteMapping("book/{bookId}")
	public ResponseEntity<ApiResponse> deleteBook(@PathVariable Long bookId){
		try {
			bookService.deleteBook(bookId);
			return ResponseEntity.ok(new ApiResponse("Delete success",bookId));
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Deleted failed",e.getMessage()));
		}
	}    
}
