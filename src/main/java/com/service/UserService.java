package com.service;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.configSecurity.JWTService;
import com.exception.AlreadyExistsException;
import com.exception.ResourceNotFoundException;
import com.model.Users;
import com.repository.UserRepo;

import jakarta.transaction.Transactional;

@Service
public class UserService {

	@Autowired
	private final UserRepo repo;

	@Autowired
	private JWTService jwtService;

	@Autowired
	private final AuthenticationManager authMgr;

	public UserService(UserRepo repo, AuthenticationManager authMgr) {
		this.repo = repo;
		this.authMgr = authMgr;
	}

	public String verifyUser(String username, String password){
		Authentication auth = authMgr.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		if(auth.isAuthenticated()) {
			return jwtService.generateToken(username);
		}else{
			throw new ResourceNotFoundException("User not found");
		}
	}
	
	public List<Users> getUsers(){
		return repo.findAll();
	}
	
	public Users getUserById(Long userId){
		return repo.findById(userId).orElseThrow(()->new ResourceNotFoundException("UserId not found: " + userId));
	}
	
	public Users getUserByEmail(String email){
		return repo.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User not found: " + email));
	}

	public Users getUserByUsername(String username){
		return repo.findByUsername(username).orElseThrow(()->new ResourceNotFoundException("User not found: " + username));
	}
	
	public Users addUser(Users user){
		
		return Optional.of(user)
				.filter(u -> !repo.existsByEmail(user.getEmail()) && !repo.existsByUsername(user.getUsername()))
				.map(req -> {
					return repo.save(user);
				}).orElseThrow(()->new AlreadyExistsException("Username/Email already exists"));
	}
	
	public void deleteUser(Long userId){
		Users user = repo.findById(userId)
    			.orElseThrow(() -> new ResourceNotFoundException("UserId not found"));
		repo.deleteById(user.getId());
	}
	
	@Transactional
	public Users updateUser(Users user){		
		return repo.findById(user.getId()).map(existingUser -> {
			existingUser.setFirstname(user.getFirstname());
			existingUser.setLastname(user.getLastname());
			existingUser.setEmail(user.getEmail());
			return repo.save(user);
		}).orElseThrow(()->new ResourceNotFoundException("UserId not found"));
	}
}

