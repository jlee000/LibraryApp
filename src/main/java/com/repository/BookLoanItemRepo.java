package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.BookLoanItem;
@Repository
public interface BookLoanItemRepo extends JpaRepository<BookLoanItem,Long> {

}
