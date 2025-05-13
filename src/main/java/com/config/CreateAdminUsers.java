package com.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.model.Users;
import com.repository.UserRepo;
import com.service.UserService;

@Component
public class CreateAdminUsers implements ApplicationListener<ApplicationReadyEvent> {
	
	@Autowired
	private UserRepo repo;

	@Autowired
	private UserService usersService;
	
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		createDefaultUsers();		
	}

	public void createDefaultUsers() {
		for(int i=1; i<=3; i++) {
			String defaultEmail = "admin"+i+"@library.com";
			if(repo.existsByEmail(defaultEmail)) {
				continue;
			}
			Users u = new Users();
			u.setUsername("admin"+i);
			u.setFirstname("first"+i);
			u.setLastname("last"+i);
			u.setEmail(defaultEmail);
			u.setPassword("pass");
			u.setRole(Role.STAFF);
			u.setEnabled(true);
			usersService.addUser(u);
			System.out.println("Default admin user created: " + u.getEmail());
		}
	}
}
