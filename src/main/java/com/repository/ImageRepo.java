package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.Image;

@Repository
public interface ImageRepo extends JpaRepository<Image, Long>{
    
}
