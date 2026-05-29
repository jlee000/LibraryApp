package com.integrationTests;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.configSecurity.JWTService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.Author;
import com.model.Book;
import com.repository.AuthorRepo;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser(username = "admin1", roles = "USER")
@MockBean(JWTService.class)
public class BookIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthorRepo authorRepo;

    @Test
    void testAddAndGetBook() throws Exception {
        Author author = authorRepo.save(new Author("AuthorName", "Bio"));
        author = authorRepo.findByName(author.getName());
        Book book = new Book(author, "Desc", "isbn", 5, "Title",BigDecimal.TEN); 

        String json = objectMapper.writeValueAsString(book);

        String response = mockMvc.perform(post("/book")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.id").exists())
            .andExpect(jsonPath("$.data.title").value("Title"))
            .andReturn()
            .getResponse()
            .getContentAsString();

        JsonNode root = objectMapper.readTree(response);
        JsonNode dataNode = root.path("data");
        Book savedBook = objectMapper.treeToValue(dataNode, Book.class);

        mockMvc.perform(get("/book/{bookId}", savedBook.getId()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.title").value("Title"))
            .andExpect(jsonPath("$.data.author.name").value("AuthorName"))
            .andExpect(jsonPath("$.data.isbn").value("isbn"));
    }

    @Test
    void testAddEmptyBookValidationFail() throws Exception {
        Book book = new Book(); 

        String json = objectMapper.writeValueAsString(book);

        mockMvc.perform(post("/books")
             .contentType(MediaType.APPLICATION_JSON)
             .content(json))
             .andExpect(status().isBadRequest());
    }
}