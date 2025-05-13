package com.model;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String biography;

    @JsonIgnore
	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Book> books;

    public Author(String biography, List<Book> books, Long id, String name) {
        this.biography = biography;
        this.books = books;
        this.id = id;
        this.name = name;
    }

    public Author(String name, String biography) {
        this.name = name;
        this.biography = biography;
    }

    public Author() {
        super();
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getBiography() {
        return biography;
    }
    public void setBiography(String biography) {
        this.biography = biography;
    }
    public List<Book> getBooks() {
        return books;
    }
    public void setBooks(List<Book> books) {
       this.books = books;
    }

    @Override
    public String toString() {
        return "Author [id=" + id + ", name=" + name + ", biography=" + biography + ", books=" + books + "]";
    }
}