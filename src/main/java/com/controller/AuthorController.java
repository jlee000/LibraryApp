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
import com.model.Author;
import com.service.AuthorService;

@RestController
public class AuthorController {
    
    @Autowired
    private AuthorService authorService;

    @GetMapping("author")
    public ResponseEntity<ApiResponse> getAllAuthors(){
        List<Author> authors = authorService.getAllAuthors();
		if(authors.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Failed",authors));
		}	
		return ResponseEntity.ok(new ApiResponse("Success",authors)); 
    }

    	@GetMapping("author/{authorId}")
	public ResponseEntity<ApiResponse> getAuthorById(@PathVariable Long authorId){
		Author author = authorService.getAuthorById(authorId);		
		if(author==null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Failed",author));
		}
		return ResponseEntity.ok(new ApiResponse("Success",author));	
	}

    	@GetMapping("author/name")
	public ResponseEntity<ApiResponse> getAuthorByName(@RequestParam String name){
		Author author = authorService.getAuthorByName(name);		
		if(author==null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Failed",author));
		}
		return ResponseEntity.ok(new ApiResponse("Success",author));
	}

    	@PostMapping("author")
	public ResponseEntity<ApiResponse> addAuthor(@RequestParam String name, @RequestParam String bio){
		try {
            authorService.addAuthor(name,bio);
		return ResponseEntity.ok(new ApiResponse("Add success",name));
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Add failed",e));
		}
	}
	
	@PutMapping("author")
	public ResponseEntity<ApiResponse> updateAuthor(@RequestBody Author a){
		try {		
			authorService.updateAuthor(a);
			return ResponseEntity.ok(new ApiResponse("Update success",a));
		}catch(AlreadyExistsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse("Update failed",e));
		}
	}
	
	@DeleteMapping("author/{authorId}")
	public ResponseEntity<ApiResponse> deleteAuthor(@PathVariable Long authorId){
		try {
			authorService.deleteAuthor(authorId);
			return ResponseEntity.ok(new ApiResponse("Delete success",authorId));
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Delete failed",e));
		}
    }    
}
