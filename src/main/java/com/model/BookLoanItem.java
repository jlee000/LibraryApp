package com.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class BookLoanItem {
	
	@Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private int quantity;

	private BigDecimal loanItemValue = setLoanItemValue();
	
	@ManyToOne
	@JoinColumn(name = "bookloan_id")
	@JsonBackReference
	private BookLoan bookloan;
	
	@ManyToOne
	@JoinColumn(name = "book_id")
	private Book book;

	
	public BookLoanItem() {
		super();
	}

	public BookLoanItem(int quantity, BookLoan bookLoan, Book book) {
		this.quantity = quantity;
		this.bookloan = bookLoan;
		this.book = book;
	}

	public BigDecimal getLoanItemValue() {
		return loanItemValue;
	}
	public BigDecimal setLoanItemValue() {
		BigDecimal value = new BigDecimal(0);
		if(this.book!=null){
			value = value.add(this.book.getPrice().multiply(BigDecimal.valueOf((long)quantity)));
		}
		this.loanItemValue = value;
		return this.loanItemValue;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public BookLoan getBookloan() {
		return bookloan;
	}
	public void setBookloan(BookLoan bookLoan) {
		this.bookloan = bookLoan;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
}
