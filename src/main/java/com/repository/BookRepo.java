package com.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.Book;

@Repository
public interface  BookRepo extends JpaRepository<Book, Long> {

    public boolean existsByTitleAndAuthorName(String title, String name);

    public Long countByTitleAndAuthorName(String title, String name);

    public Long countByTitle(String title);

    @Query("SELECT COUNT(b) FROM Book b WHERE b.author.name = ?1")
    public Long countByAuthorName(String name);

    public List<Book> findByTitle(String title);

    public List<Book> findByAuthorName(String name);

  
    
}
