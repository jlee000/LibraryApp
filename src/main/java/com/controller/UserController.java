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
import org.springframework.web.bind.annotation.RestController;

import com.config.ApiResponse;
import com.exception.ResourceNotFoundException;
import com.model.Users;
import com.service.CartService;
import com.service.UserService;

import jakarta.validation.Valid;

@RestController
//@CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class UserController {
	
	@Autowired
	UserService userService;

	@Autowired
    private CartService cartService;
	
	
	@GetMapping("user")
	public ResponseEntity<ApiResponse> getUsers() {
		try{
			List<Users> users = userService.getUsers();
			for(Users u: users){
				if(u.getCart() != null && u.getCart().getCartItems() != null){
				u.getCart().setCartItems(cartService.removeCartItemImages(
					cartService.getCartByUserId(u.getId()).getCartItems()));
				}
			}
			return ResponseEntity.ok(new ApiResponse("Get user success",users));
		}catch(ResourceNotFoundException e){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Get user failed",e.getMessage()));
		}
	}
	
	@GetMapping("user/id/{userId}")
	public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
		try {
		Users u = userService.getUserById((long) userId);		
		if(u.getCart() != null && u.getCart().getCartItems() != null){
			u.getCart().setCartItems(cartService.removeCartItemImages(
			cartService.getCartByUserId(u.getId()).getCartItems()		));
		}
		return ResponseEntity.ok(new ApiResponse("Get users success",u));
		}catch(ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Get user failed",e.getMessage()));
		}
	}
	
	@GetMapping("user/email")
	public ResponseEntity<ApiResponse> getUserByEmail(@RequestBody String email) {
		try {
		Users u = userService.getUserByEmail(email);		
		if(u.getCart() != null && u.getCart().getCartItems() != null){
			u.getCart().setCartItems(cartService.removeCartItemImages(
			cartService.getCartByUserId(u.getId()).getCartItems()		));
		}
		return ResponseEntity.ok(new ApiResponse("Get user success",u));
		}catch(ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Get user failed",e.getMessage()));
		}
	}

	@GetMapping("user/username")
	public ResponseEntity<ApiResponse> getUserByUsername(@RequestBody String username) {
		try {
		Users u = userService.getUserByUsername(username);
		if(u.getCart() != null && u.getCart().getCartItems() != null){
			u.getCart().setCartItems(cartService.removeCartItemImages(
					cartService.getCartByUserId(u.getId()).getCartItems()		));
		}
		return ResponseEntity.ok(new ApiResponse("Get user success",u));
		}catch(ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Get user failed",e.getMessage()));
		}
	}
	
	@PostMapping("user")
	public ResponseEntity<ApiResponse> addUser(@Valid @RequestBody Users user) {
		try {
			Users u = userService.addUser(user);
			return ResponseEntity.ok(new ApiResponse("Add user success",u));
			}catch(Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Add user failed",e.getMessage()));
			}
	}
	
	@PutMapping("user")
	public ResponseEntity<ApiResponse> updateUser(@RequestBody Users user) {
		try {
			Users u = userService.updateUser(user);
			return ResponseEntity.ok(new ApiResponse("Update user success",u));
			}catch(Exception e) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Update user failed",e.getMessage()));
			}
	}
	
	@DeleteMapping("user/{userId}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
		try {
			userService.deleteUser(userId);
			return ResponseEntity.ok(new ApiResponse("Delete user success",userId));
			}catch(Exception e) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Delete user failed",e.getMessage()));
			}
	}
}
