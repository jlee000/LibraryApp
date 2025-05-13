package com.integrationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.config.Role;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.Cart;
import com.model.Users;

@SpringBootTest
@AutoConfigureMockMvc
public class UserIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddAndGetUser() throws Exception {
        Users user = new Users("Username","pass","First","Last","email@email.com", Role.MEMBER, true, null, null);
        user.setCart(new Cart(1L, user,null));

        String json = objectMapper.writeValueAsString(user);

        String response = mockMvc.perform(post("/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.id").exists())
            .andExpect(jsonPath("$.data.username").value("Username"))
            .andReturn()
            .getResponse()
            .getContentAsString();

        JsonNode root = objectMapper.readTree(response);
        JsonNode dataNode = root.path("data");
        Users savedUser = objectMapper.treeToValue(dataNode, Users.class);

        mockMvc.perform(get("/user/id/{userId}", savedUser.getId()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.username").value("Username"))
            .andExpect(jsonPath("$.data.firstname").value("First"));
    }

    @Test
    void testAddEmptyUserValidationFail() throws Exception {
        Users user = new Users(); 

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user")
             .contentType(MediaType.APPLICATION_JSON)
             .content(json))
             .andDo(print())
             .andExpect(status().isBadRequest()); //Add @Valid to UserController, Add @NotBlank/@Email to Entity, to allow Spring to interpret a Users object with not null properties as isBadRequest instead of isInternalServerError
    }
}
