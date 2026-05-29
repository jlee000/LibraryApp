package com.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.Book;
import com.model.Cart;
import com.model.Users;
import com.repository.CartItemRepo;
import com.repository.CartRepo;

import jakarta.transaction.Transactional;

@Service
public class CartService {
    
    @Autowired
    private CartRepo cartRepo;

    @Autowired
	private CartItemRepo cartItemRepo;
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private UserService userService;
	
	private final AtomicLong cartIdGenerator = new AtomicLong(0);
	
	
	public Cart getCartByCartId(Long cartId) {
		Cart cart = cartRepo.findById(cartId)
			.orElse(initNewCart(new Users()));
		return cartRepo.save(cart);
	}
	
	public Cart getCartByUserId(Long userId) {
		Cart cart = cartRepo.findByUserId(userId);
		if(cart == null) {
			cart = initNewCart(userService.getUserById(userId));
		}
		return cartRepo.save(cart);
	}

	public Cart getCartByUsername(String username) {
		Cart cart = cartRepo.findByUsername(username);
		if(cart == null) {
			cart = initNewCart(userService.getUserByUsername(username));
		}
		return cartRepo.save(cart);
	}
	
	@Transactional
	public void clearCart(Long id) {
		Cart cart = getCartByCartId(id);
		cartItemRepo.deleteAllByCartId(id);
		cart.getCartItems().stream().forEach(item -> {
			Book b = bookService.getBookById(item.getBook().getId());
			b.setStock(b.getStock() + item.getQuantity());
			bookService.updateBook(b);
		});
		cart.getCartItems().clear();
		cartRepo.save(cart);	
	}
	
	public Cart initNewCart(Users user) {		
		return new Cart(cartIdGenerator.incrementAndGet(), user, new ArrayList<>(Arrays.asList()));				
	}
}
