package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.config.ApiResponse;
import com.model.Cart;
import com.service.CartService;

@RestController
public class CartController {
    
    @Autowired
    private CartService cartService;

    @GetMapping("cart/id/{cartId}")
	public ResponseEntity<ApiResponse> getCartById(@PathVariable Long cartId){
		Cart cart = cartService.getCartByCartId(cartId);
		if(cart ==null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Failed",cartId));	
		}
		return ResponseEntity.ok(new ApiResponse("Success",cart));
	}	

	@GetMapping("cart/userid/{userId}")
	public ResponseEntity<ApiResponse> getCartByUserId(@PathVariable Long userId){
		Cart cart = cartService.getCartByUserId(userId);
		if(cart ==null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Failed",userId));	
		}
		return ResponseEntity.ok(new ApiResponse("Success",cart));
	}	

	@GetMapping("cart/username/{username}")
	public ResponseEntity<ApiResponse> getCartByUsername(@PathVariable String username){
		Cart cart = cartService.getCartByUsername(username);
		if(cart ==null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Failed",username));	
		}
		return ResponseEntity.ok(new ApiResponse("Success",cart));
	}	
	
	@DeleteMapping("cart/{cartId}")
	public ResponseEntity<ApiResponse> clearCart(@PathVariable Long cartId){
		try {
		cartService.clearCart(cartId);
		return ResponseEntity.ok(new ApiResponse("Clear cart success",cartId));
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Delete failed",e));
		}
	}	
}
