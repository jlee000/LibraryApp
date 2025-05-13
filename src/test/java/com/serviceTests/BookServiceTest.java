package com.serviceTests;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.model.Author;
import com.model.Book;
import com.repository.BookRepo;
import com.service.AuthorService;
import com.service.BookService;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    
    @Mock
    private BookRepo bookRepo;

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private BookService bookService;

    @Test
    void testAddBook() {
        Author author = new Author("Name","Bio");
        Book book = new Book(author,"Desc","isbn",5,"Title",BigDecimal.TEN);
        when(authorService.getAuthorByName(author.getName())).thenReturn(author);
        when(bookRepo.save(any(Book.class))).thenReturn(book);

        Book saved = bookService.addBook(book);

        assertNotNull(saved);
        assertEquals("Title", saved.getTitle());
        verify(bookRepo, times(1)).save(book);
    }
    
    @Test
    void testGetBookById() {
        Author author = new Author("Name","Bio");
        Book book = new Book(author,"Desc","isbn",5,"Title",BigDecimal.TEN);
        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));

        Book foundBook = bookService.getBookById(1L);

        assertTrue(foundBook != null);
        assertEquals("Title", foundBook.getTitle());
    }

    @Test
    void testGetAllBooks() {
        List<Book> books = Arrays.asList(
                new Book(new Author("Name","Bio"),"Desc","isbn",5,"Title",BigDecimal.TEN),
                new Book(new Author("Namee","Bioo"),"Descc","isbnn",55,"Titlee",BigDecimal.ONE)
        );
        when(bookRepo.findAll()).thenReturn(books);

        List<Book> result = bookService.getBooks();

        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
