package com.service;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exception.AlreadyExistsException;
import com.exception.ResourceNotFoundException;
import com.model.Users;
import com.repository.UserRepo;

import jakarta.transaction.Transactional;

@Service
public class UserService {

	@Autowired
	private final UserRepo repo;

	public UserService(UserRepo repo) {
		this.repo = repo;
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

