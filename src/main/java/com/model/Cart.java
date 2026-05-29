package com.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Cart {
    
    @Id
    Long id;

    @OneToOne
    @JoinColumn(name="user_id")
	@JsonIgnore
    private Users user;

    @OneToMany(mappedBy="cart", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<Cartitem> cartItems;

    public Cart(Long id, Users user, List<Cartitem> cartItems) {
        this.id = id;
        this.user = user;
        this.cartItems = cartItems;
    }

    public Cart() {
    }
    
    public Long getId() {
        return id;
    }
    public Users getUser() {
        return user;
    }
    public void setUser(Users user) {
        this.user = user;
    }
    public List<Cartitem> getCartItems() {
        return cartItems;
    }
    public void setCartItems(List<Cartitem> cartItems) {
        this.cartItems = cartItems;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cart{");
        sb.append("id=").append(id);
        sb.append(", user=").append(user);
        sb.append(", cartItems=").append(cartItems);
        sb.append('}');
        return sb.toString();
    }   

    	
	public void addItem(Cartitem item) {
		this.cartItems.add(item);
		item.setCart(this);
		
	}
	
	public void removeItem(Cartitem item) {
		this.cartItems.remove(item);
		item.setCart(null);	
	}
}
