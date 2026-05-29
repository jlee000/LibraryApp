package com.controllerTests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import com.config.Role;
import com.configSecurity.JWTService;
import com.controller.UserController;
import com.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.model.Cart;
import com.model.Users;
import com.repository.UserRepo;
import com.service.CartService;
import com.service.UserService;

@WebMvcTest(UserController.class)
@WithMockUser(username = "admin1", roles = "ADMIN")
@MockBean(JWTService.class)
public class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepo userRepo;

    @MockBean 
    private UserService userService;

    @MockBean 
    private CartService cartService;

    @Test
    void testGetUserById() throws Exception {
        Users user = new Users("Username","pass","First","Last","email@email.com", Role.MEMBER, true, null, null);
        user.setCart(new Cart(1L, user,null));
        when(userService.getUserById(1L)).thenReturn(user);
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(cartService.getCartByUserId(1L)).thenReturn(new Cart(1L, user,null));

        mockMvc.perform(get("/user/id/1"))            
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.username").value("Username"))
            .andExpect(jsonPath("$.data.firstname").value("First"));
    }

    @Test
    void testAddUser() throws Exception {
        Users user = new Users("Username","pass","First","Last","email@email.com", Role.STAFF, true, null, null);
        when(userService.addUser(any(Users.class))).thenReturn(user);
        when(cartService.getCartByUserId(1L)).thenReturn(new Cart(1L, user,null));
        
        final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mockMvc.perform(post("/user")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.username").value("Username"))
            .andExpect(jsonPath("$.data.firstname").value("First"));
    }

    @Test
    void testGetUserByBadId_Fail() throws Exception {
        when(userService.getUserById(10L)).thenThrow(new ResourceNotFoundException("UserId not found"));

        mockMvc.perform(get("/user/id/10"))
            .andExpect(status().isNotFound());
    }
}
