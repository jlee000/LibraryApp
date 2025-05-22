package com.configSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.model.Users;
import com.repository.UserRepo;

@Service
public class UserDetailsServiceCustom implements UserDetailsService{
	
	@Autowired
	private UserRepo repo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = repo.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("Username not found: "+ username));
		
		return new UserDetailsCustom(user);
	}

}
