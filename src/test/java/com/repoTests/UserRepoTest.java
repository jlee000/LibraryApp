package com.repoTests;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.config.Role;
import com.model.Users;
import com.repository.UserRepo;

@DataJpaTest
public class UserRepoTest {

    @Autowired
    private UserRepo userRepo;

    @Test
    void testAddAndFindUser() {
        Users user = new Users("Username","pass","First","Last","email@email.com", Role.MEMBER, true, null, null);

        Users savedUser = userRepo.save(user);

        Optional<Users> found = userRepo.findById(savedUser.getId());

        assertTrue(found.isPresent());
        assertEquals("Username", found.get().getUsername());
    }

    @Test
    void testFindByUsername() {
        userRepo.save(new Users("Username","pass","First","Last","email@email.com", Role.MEMBER, true, null, null));

        Optional<Users> user = userRepo.findByUsername("Username");

        assertTrue(user.isPresent());
        assertEquals("Username", user.get().getUsername());
    }

    @Test
    void testDeleteUser() {
        Users user = new Users("Username","pass","First","Last","email@email.com", Role.MEMBER, true, null, null);

        userRepo.delete(user);

        assertFalse(userRepo.findByUsername(user.getUsername()).isPresent());
    }
    
}
