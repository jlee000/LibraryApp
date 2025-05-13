package com.modelTests;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.model.Author;
import com.model.Book;

public class BookTest {
    
    @Test
    void testBookConstructorAndGettersSetters() {
        Book book = new Book(1L, "Title", "isbn","Desc",5,BigDecimal.TEN, new Author("AuthorName","Bio"), null);

        assertEquals(1L, book.getId());
        assertEquals("Title", book.getTitle());
        assertEquals("isbn", book.getIsbn());
        assertEquals("Desc", book.getDescription());
        assertEquals(5, book.getStock());
        assertEquals(BigDecimal.TEN, book.getPrice());
        assertEquals("AuthorName", book.getAuthor().getName());
    }
}
