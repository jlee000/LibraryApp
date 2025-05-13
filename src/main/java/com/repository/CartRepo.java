package com.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.Cart;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long>{

    @Query("SELECT s FROM Cart s WHERE s.user_id = ?1")
    Cart findByUserId(Long userId);
}
