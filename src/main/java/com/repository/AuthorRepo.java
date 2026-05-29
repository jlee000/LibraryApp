package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.Author;

@Repository
public interface AuthorRepo extends JpaRepository<Author,Long>{

    public Author findByName(String name);
    
}
