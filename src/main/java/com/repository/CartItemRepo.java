package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.Cartitem;

@Repository
public interface CartItemRepo extends JpaRepository<Cartitem, Long>{

    public void deleteAllByCartId(Long id);
    
}
