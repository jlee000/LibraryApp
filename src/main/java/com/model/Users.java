package com.model;
import java.time.LocalDate;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.config.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Users {
	
	@Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	@NotBlank(message = "Username must not be blank")
	private String username;

	@Column(nullable = false)
	@NotBlank(message = "Password must not be blank")
	private String password;

	private String firstname;

	private String lastname;

	@Column(nullable = false, unique = true)
	@Email(message = "Email should be valid")
    @NotBlank(message = "Email must not be blank")
	private String email;

	private LocalDate createdOn = LocalDate.now();

	private LocalDate updatedOn;

	@Enumerated(EnumType.STRING)
	private Role Role;
	private boolean active;

	@OneToOne(mappedBy = "user",  cascade=CascadeType.ALL, orphanRemoval=true)
	private Cart cart;

	@OneToMany(mappedBy = "user",  cascade=CascadeType.ALL, orphanRemoval=true)
	private List<BookLoan> bookLoans;

	@Transient
	@JsonIgnore
	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
	
	public Users() {
		super();
	}

	public Users(String username, String password, String firstname, String lastname, String email, com.config.Role role, boolean enabled, Cart cart, List<BookLoan> bookLoans) {
		this.username = username;
		this.email = email;
		this.password = encoder.encode(password);
		this.firstname = firstname;
		this.lastname = lastname;
		Role = role;
		this.active = enabled;
		this.cart = cart;
		this.bookLoans=bookLoans;
	}

	public Users(String username, String password, String firstname, String lastname, String email, com.config.Role role, boolean enabled) {
		this.username = username;
		this.email = email;
		this.password = encoder.encode(password);
		this.firstname = firstname;
		this.lastname = lastname;
		Role = role;
		this.active = enabled;
	}

	public Cart getCart() {
		return cart;
	}
	public void setCart(Cart cart) {
		this.cart = cart;
	}
	public List<BookLoan> getLoan() {
		return bookLoans;
	}
	public void setLoan(List<BookLoan> bookLoans) {
		this.bookLoans = bookLoans;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = encoder.encode(password);
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public LocalDate getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(LocalDate createdOn) {
		this.createdOn = createdOn;
	}
	public LocalDate getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(LocalDate updatedOn) {
		this.updatedOn = updatedOn;
	}
	public Role getRole() {
		return Role;
	}
	public void setRole(Role role) {
		Role = role;
	}

	@Override
	public String toString() {
		return "Users [id=" + id + ", username=" + username + ", password=" + password + ", firstname=" + firstname
				+ ", lastname=" + lastname + ", email=" + email + ", createdOn=" + createdOn + ", updatedOn="
				+ updatedOn + ", Role=" + Role + ", enabled=" + active + ", cart=" + cart + ", loan=" + bookLoans + "]";
	}
}
