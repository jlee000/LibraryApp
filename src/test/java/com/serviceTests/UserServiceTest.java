package com.serviceTests;

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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.config.Role;
import com.model.Users;
import com.repository.UserRepo;
import com.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    
    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserService userService;


    @Test
    void testAddUser() {
        Users user = new Users("Username","pass","First","Last","email@email.com", Role.MEMBER, true, null, null);
        when(userRepo.save(any(Users.class))).thenReturn(user);

        Users saved = userService.addUser(user);

        assertNotNull(saved);
        assertEquals("Username", saved.getUsername());
        verify(userRepo, times(1)).save(user);
    }
    
    @Test
    void testGetUserById() {
        Users user = new Users("Username","pass","First","Last","email@email.com", Role.MEMBER, true, null, null);
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        Users foundUser = userService.getUserById(1L);

        assertTrue(foundUser != null);
        assertEquals("Username", foundUser.getUsername());
    }

    @Test
    void testGetAllUsers() {
        List<Users> users = Arrays.asList(
             new Users("Username","pass","First","Last","email@email.com", Role.MEMBER, true, null, null) ,
             new Users("Usernamee","passs","Firstt","Lastt","emaill@email.com", Role.MEMBER, true, null, null)
        );
        when(userRepo.findAll()).thenReturn(users);

        List<Users> result = userService.getUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testDeleteUser() {
        Users user = new Users(); user.setId(1L);
        
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepo).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepo, times(1)).deleteById(1L);
    }
}
