package com.configSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private UserDetailsServiceCustom userDetailServiceCustom;
	
	@Autowired
	private JWTFilter jwtFilter;

	@Bean
	public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
		
		http
			//Disable csrf
			.csrf(csrf -> csrf.disable())

			// CORS configuration
            .cors().and()
			
			//Enable login feature
			.authorizeHttpRequests(request-> request	
				.requestMatchers("/home","/login", "/register").permitAll()//Permit these endpoints without authentication
				.requestMatchers("/h2-console/**").permitAll() //Permit H2 console access without authentication
				.anyRequest().authenticated())//other requests require authentication

			//Spring Security form login
			//.formLogin(Customizer.withDefaults())//for spring security basic login feature		
			
			//Enable HTTP Basic Authentication
			.httpBasic(Customizer.withDefaults())
			
			//Make http stateless, allow spring security to manage SessionId
			.sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

			//Disable frame options to allow H2 console
			.headers().frameOptions().disable();
			
		return http.build();
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider () {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
		provider.setUserDetailsService(userDetailServiceCustom);
		return provider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	//Global CORS config for React frontend localhost:3000
	@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                    .allowedOrigins("http://localhost:3000")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
					.allowCredentials(true);
            }
        };
    }
}
