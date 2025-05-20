package com.it.airbnb.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity // Enables Spring Security for the application
@RequiredArgsConstructor // Automatically injects final fields via constructor (e.g., jwtAuthFilter)
public class WebSecurityConfig {

    // Custom JWT authentication filter to validate tokens before standard authentication
    private final JwtAuthFilter jwtAuthFilter;
    
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;


    // Bean to configure the main security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                // Disable CSRF because we are using token-based (stateless) authentication
                .csrf(csrfConfig -> csrfConfig.disable())

                // Use stateless session management — no sessions will be created or used
                .sessionManagement(sessionConfig -> 
                    sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Add the JWT filter before Spring's built-in UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                // Authorization configuration for different URL patterns
                .authorizeHttpRequests(auth -> auth
                        // Only users with role "HOTEL_MANAGER" can access /admin/** endpoints
                        .requestMatchers("/admin/**").hasRole("HOTEL_MANAGER")

                        // All authenticated users (any role) can access /bookings/**
                        .requestMatchers("/bookings/**").authenticated()
                        
                        // All authenticated users (any role) can access /users/**
                        .requestMatchers("/users/**").authenticated()

                        // Allow public access to all other endpoints
                        .anyRequest().permitAll()
                )
                .exceptionHandling(exHandlingConfig -> exHandlingConfig.accessDeniedHandler(accessDeniedHandler()))

                // Build the SecurityFilterChain object
                .build();
    }

    // Bean for password encryption using BCrypt hashing algorithm
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Recommended and secure
    }

    // Bean to expose AuthenticationManager (used in authentication logic, e.g., in login service)
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
    	
    	return (request, response, accessDeniedException) -> {
    		handlerExceptionResolver.resolveException(request, response, null, accessDeniedException);
    	};
    }
}
