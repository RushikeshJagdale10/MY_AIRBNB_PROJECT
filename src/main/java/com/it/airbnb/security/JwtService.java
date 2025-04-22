package com.it.airbnb.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.it.airbnb.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	@Value("${jwt.secretKey}")
	private String jwtSecretKey;
	
	private SecretKey getSecretKey() {
		return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
		
	}
	
	public String generateAccessToken(User user) {
		
		String token =	Jwts.builder()
							.subject(user.getId().toString())
							.claim("email", user.getEmail())
							.claim("roles", user.getRoles().toString())
							.issuedAt(new Date())
							.expiration(new Date(System.currentTimeMillis() + 1000*60*10))
							.signWith(getSecretKey())
							.compact();
		
		System.out.println("Token: " + token);
		
		return token;
		
	}
	
public String generateRefreshToken(User user) {
		
		String token =	Jwts.builder()
							.subject(user.getId().toString())
							.issuedAt(new Date())
							.expiration(new Date(System.currentTimeMillis() + 1000L *60*60*24*30*6))
							.signWith(getSecretKey())
							.compact();
		
		System.out.println("Token: " + token);
		
		return token;
		
	}
	
	public Long getUserIdFromToken(String token) {
		
		Claims claims = Jwts.parser()
						  .verifyWith(getSecretKey())
						  .build()
						  .parseSignedClaims(token)
						  .getPayload();
						  
		System.out.println("Id: " + claims.getId());
		return Long.valueOf(claims.getSubject());
		
	}
	
}
