package com.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.config.ApiResponse;
import com.config.LoginRequest;
import com.configSecurity.UserDetailsCustom;
import com.model.Users;
import com.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/loggedinuser")
    public ResponseEntity<ApiResponse> getCurrentUser(Authentication authentication) {
        try{
            UserDetailsCustom userDetails = (UserDetailsCustom)authentication.getPrincipal();
            Users user = userService.getUserByUsername(userDetails.getUsername());
            return ResponseEntity.ok(new ApiResponse("Success",user));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("User not logged in", e.getMessage()));
        }
    }

    @PostMapping("login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response){
        try{
            String jwt = userService.verifyUser(loginRequest.getUsername(),loginRequest.getPassword());

            Cookie jwtCookie = new Cookie("JWT", jwt);
            //jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(false);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(3600); //Expire in 1 hour
            response.addCookie(jwtCookie);

            return ResponseEntity.ok(new ApiResponse("Login Success",jwt));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Login failed", e.getMessage()));
        }
    }

    @PostMapping("register")
    public ResponseEntity<ApiResponse> register(@RequestBody Users user){
        try{
            Users u = userService.addUser(user);
            return ResponseEntity.ok(new ApiResponse("User registered successfully",u));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Register failed", e.getMessage()));
        }
    }
}
