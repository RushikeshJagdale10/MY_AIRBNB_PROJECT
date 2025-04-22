package com.it.airbnb.controller;

import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.it.airbnb.dto.LoginDto;
import com.it.airbnb.dto.LoginResponseDto;
import com.it.airbnb.dto.SignUpRequestDto;
import com.it.airbnb.dto.UserDto;
import com.it.airbnb.security.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
	
	private final AuthService authService;
	
	@PostMapping("/signup")
	public ResponseEntity<UserDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {	
		
		return new ResponseEntity<>(authService.signUp(signUpRequestDto), HttpStatus.CREATED);
	
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {	
		
		String[] tokens = authService.login(loginDto);
 		
		Cookie cookie = new Cookie("refreshToken", tokens[1]);
		cookie.setHttpOnly(true);
		
		httpServletResponse.addCookie(cookie);
		
		return ResponseEntity.ok(new LoginResponseDto(tokens[0]));
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<LoginResponseDto> refresh(HttpServletRequest httpServletRequest) {	
		
		String refreshToken = Arrays.stream(httpServletRequest.getCookies())
				.filter(cookie -> "refreshToken".equals(cookie.getName()))
				.findFirst()
				.map(Cookie::getValue)
				.orElseThrow(() -> new AuthenticationServiceException("Refresh Token Not Found Inside The Cookies!!!"));
				
		String accessToken = authService.refreshToken(refreshToken);
		
		return ResponseEntity.ok(new LoginResponseDto(accessToken));
	}

}
