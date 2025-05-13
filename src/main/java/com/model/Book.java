package com.model;
import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Book {
    @Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;	
    @Column(nullable = false)
	private String title;
    @Column(nullable = false, unique = true)
	private String isbn;
	private String description;	
    @Column(nullable = false)
	private int stock;
    @Column(nullable = false)
    private BigDecimal price;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="author_id",nullable = false)
	private Author author;

    @OneToMany(mappedBy="book", cascade=CascadeType.ALL, orphanRemoval=(true))
	private List<Image> images;

    public Book() {
    }

    public Book(Long id, String title, String isbn, String description, int stock, BigDecimal value, Author author, List<Image> images) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.description = description;
        this.stock = stock;
        this.price = value;
        this.author = author;
        this.images = images;
    }

    public Book(Author author, String description, String isbn, int stock, String title, BigDecimal price) {
        this.author = author;
        this.description = description;
        this.isbn = isbn;
        this.stock = stock;
        this.title = title;
        this.price = price;
    }

    public List<Image> getImages() {
        return images;
    }
    public void setImages(List<Image> images) {
        this.images = images;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getStock() {
        return stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
    public Author getAuthor() {
        return author;
    }
    public void setAuthor(Author author) {
        this.author = author;
    }    
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal value) {
        this.price = value;
    }

    @Override
    public String toString() {
        return "Book [id=" + id + ", title=" + title + ", isbn=" + isbn + ", description=" + description + ", stock="
                + stock + ", value=" + price + ", author=" + author + ", images=" + images + "]";
    }	
}
