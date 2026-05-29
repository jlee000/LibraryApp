package com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.BookLoan;

@Repository
public interface BookLoanRepo extends JpaRepository<BookLoan, Long>{
    List<BookLoan> findByUserId(Long userId);
}
