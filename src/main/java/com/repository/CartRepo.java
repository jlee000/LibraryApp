package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.model.Cart;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long>{

    @Query("SELECT s FROM Cart s WHERE s.user.id = ?1")
    Cart findByUserId(Long userId);

    @Query("SELECT s FROM Cart s WHERE s.user.username = ?1")
    Cart findByUsername(String username);
}
