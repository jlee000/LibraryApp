package com.repoTests;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.model.Author;
import com.model.Book;
import com.repository.AuthorRepo;
import com.repository.BookRepo;

@DataJpaTest
public class BookRepoTest {

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private AuthorRepo authorRepo;

    @Test
    void testAddAndFindBook() {
        Author author = new Author("AuthorName", "Bio");
        Author savedAuthor = authorRepo.save(author);

        Book book = new Book(savedAuthor, "Desc", "isbn", 10, "Title",BigDecimal.TEN);

        Book savedBook = bookRepo.save(book);

        Optional<Book> found = bookRepo.findById(savedBook.getId());

        assertTrue(found.isPresent());
        assertEquals("Title", found.get().getTitle());
        assertEquals("AuthorName", found.get().getAuthor().getName());
    }

    @Test
    void testFindByTitle() {
        Author author = authorRepo.save(new Author("AuthorName", "Bio"));
        bookRepo.save(new Book(author, "Desc", "isbn", 5, "Title", BigDecimal.TEN));

        List<Book> books = bookRepo.findByTitle("Title");

        assertTrue(!books.isEmpty());
        assertEquals("Title", books.get(0).getTitle());
    }

    @Test
    void testDeleteBook() {
        Author author = authorRepo.save(new Author("AuthorName", "Bio"));
        Book book = bookRepo.save(new Book(author, "Desc", "isbn", 1, "Title", BigDecimal.TEN));

        bookRepo.delete(book);

        assertFalse(bookRepo.findById(book.getId()).isPresent());
    }
}

