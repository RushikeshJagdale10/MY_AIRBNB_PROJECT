package com.it.airbnb.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.it.airbnb.entity.User;
import com.it.airbnb.service.UserService;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    // Inject JwtService for token-related operations like extracting userId from JWT
    private final JwtService jwtService;

    // Inject UserService to load user details from the database using userId
    private final UserService userService;
    
    
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

    	try {
    	
		        // Step 1: Get the JWT token from the Authorization header
		        final String requestTokenHeader = request.getHeader("Authorization");
		
		        // Step 2: If the Authorization header is missing or doesn't start with "Bearer", 
		        // skip filtering and continue the filter chain
		        if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer")) {
		            filterChain.doFilter(request, response);
		            return;
		        }
		
		        // Step 3: Extract the JWT token by removing the "Bearer " prefix
		        String token = requestTokenHeader.split("Bearer ")[1];
		
		        // Step 4: Extract the userId from the token using JwtService
		        Long userId = jwtService.getUserIdFromToken(token);
		
		        // Step 5: Check if userId is valid and no authentication is set in the security context
		        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
		
		            // Step 6: Load user details using userId
		            User user = userService.getUserById(userId);
		
		            // Step 7: Create an authentication token with user's authorities (roles/permissions)
		            UsernamePasswordAuthenticationToken authenticationToken =
		                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		
		            // Step 8: Set request details like remote address, session ID, etc., into the token
		            authenticationToken.setDetails(
		                    new WebAuthenticationDetailsSource().buildDetails(request)
		            );
		
		            // Step 9: Set the authentication token in the SecurityContext
		            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		        }
		
		        // Step 10: Continue with the next filter in the filter chain
		        filterChain.doFilter(request, response);
		    	
    	} catch(JwtException ex) {
    		handlerExceptionResolver.resolveException(request, response, null, ex);
    	}
    }
}

