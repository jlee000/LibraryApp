package com.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Cartitem {
    @Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="book_id")
	private Book book;
	
	private int quantity;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="cart_id")
	private Cart cart;
    
    public Cartitem(Long id, Book book, int quantity, Cart cart) {
        this.id = id;
        this.book = book;
        this.quantity = quantity;
        this.cart = cart;
    }

    public Cartitem(Book book, int quantity, Cart cart) {
        this.book = book;
        this.quantity = quantity;
    }

    public Cartitem() {
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Book getBook() {
        return book;
    }
    public void setBook(Book book) {
        this.book = book;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public Cart getCart() {
        return cart;
    }
    public void setCart(Cart cart) {
        this.cart = cart;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cartitem{");
        sb.append("id=").append(id);
        sb.append(", book=").append(book);
        sb.append(", quantity=").append(quantity);
        sb.append(", cart=").append(cart);
        sb.append('}');
        return sb.toString();
    }


}
