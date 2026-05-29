package com.controllerTests;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Optional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import com.configSecurity.JWTService;
import com.controller.BookController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.Author;
import com.model.Book;
import com.repository.BookRepo;
import com.service.BookService;

@WebMvcTest(BookController.class)
@WithMockUser(username = "admin1", roles = "USER")
@MockBean(JWTService.class)
public class BookControllerTest {
        
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepo bookRepo;

    @MockBean 
    private BookService bookService;

    @Test
    void testGetBookById() throws Exception {
        Book book = new Book(new Author("Name","Bio"),"Desc","isbn",5,"Title", BigDecimal.TEN);
        when(bookService.getBookById(1L)).thenReturn(book);
        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));

        mockMvc.perform(get("/book/1"))            
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.title").value("Title"))
            .andExpect(jsonPath("$.data.isbn").value("isbn"));
    }

    @Test
    void testAddBook() throws Exception {
        Book inputBook = new Book(new Author("Name", "Bio"), "Desc", "isbn", 5, "Title", BigDecimal.TEN);
        when(bookService.addBook(any(Book.class))).thenReturn(inputBook);

        final ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/book")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(inputBook)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.title").value("Title"))
            .andExpect(jsonPath("$.data.author.name").value("Name"));
    }
}
