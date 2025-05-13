package com.repository;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.Users;


@Repository
public interface UserRepo extends JpaRepository<Users,Long> {
	
	@Query("SELECT s FROM Users s WHERE s.email = ?1")
	Optional<Users> findByEmail(String email);
	
	@Query("SELECT s FROM Users s WHERE s.username = ?1")
	Optional<Users> findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);
}
