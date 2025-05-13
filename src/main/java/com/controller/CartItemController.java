package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.config.ApiResponse;
import com.exception.ResourceNotFoundException;
import com.model.Cart;
import com.model.Users;
import com.service.CartItemService;
import com.service.CartService;
import com.service.UserService;

@RestController
public class CartItemController {
    
    @Autowired
    private CartItemService cartItemService;

	@Autowired
	private UserService userService;

	@Autowired
	private CartService cartService;
	
	
	@PostMapping("cartitem")
	public ResponseEntity<ApiResponse> addCartItem(@RequestParam Long bookId, @RequestParam Integer quantity, @RequestParam String username){
		try {
			Users u = userService.getUserByUsername(username);
			Cart cart = cartService.getCartByUserId(u.getId());		
			cartItemService.addCartItem(cart.getId(), bookId, quantity);
			return ResponseEntity.ok(new ApiResponse("Add Success",cart.getCartItems()));
		}catch(ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Add failed",e));
		}
	}
	
	@DeleteMapping("cartitem/{cartId}/{bookId}")
	public ResponseEntity<ApiResponse> removeCartItem(@PathVariable Long cartId,
													@PathVariable Long bookId){
		try {
			cartItemService.removeCartItem(cartId, bookId);
			return ResponseEntity.ok(new ApiResponse("Delete Success",cartId));
		}catch(ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Delete failed",e));
		}
	}
	
	@PutMapping("cartitem/{cartId}/{bookId}")
	public ResponseEntity<ApiResponse> updateCartItemQuantity(@PathVariable Long cartId, @PathVariable Long bookId, @RequestParam Integer quantity){
		try {
			cartItemService.updateCartItemQuantity(cartId, bookId, quantity);
			return ResponseEntity.ok(new ApiResponse("Update Success",cartId));
		}catch(ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Update failed",e));
		}
	}    
}
