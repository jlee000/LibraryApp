package com.modelTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.config.Role;
import com.model.Users;

public class UsersTest {
    @Test
    void testBookConstructorAndGettersSetters() {
        Users user = new Users("Username","pass","First","Last","email@email.com", Role.MEMBER, true, null, null);

        assertEquals("Username", user.getUsername());
        assertEquals("First", user.getFirstname());
        assertEquals("Last", user.getLastname());
        assertEquals("email@email.com", user.getEmail());
        assertEquals(Role.MEMBER, user.getRole());
    }
}