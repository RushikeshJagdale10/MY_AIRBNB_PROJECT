package com.it.airbnb.security;

import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.it.airbnb.dto.LoginDto;
import com.it.airbnb.dto.SignUpRequestDto;
import com.it.airbnb.dto.UserDto;
import com.it.airbnb.entity.User;
import com.it.airbnb.enums.Role;
import com.it.airbnb.exception.ResourceNotFoundException;
import com.it.airbnb.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	
	public UserDto signUp(SignUpRequestDto signUpRequestDto) {
		
		User user = userRepository.findByEmail(signUpRequestDto.getEmail()).orElse(null);
		
		if(user != null) {
			throw new RuntimeException("User is already present with same email id");
		}
		
		
		User newUser = modelMapper.map(signUpRequestDto, User.class);
		newUser.setRoles(Set.of(Role.GUEST));
		newUser.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));
		newUser = userRepository.save(newUser);
		
		
		return modelMapper.map(newUser, UserDto.class);
	}
	
	public String[] login(LoginDto loginDto) {
		
		Authentication authentication = 
					authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()
		));
		
		User user = (User) authentication.getPrincipal();
		
		String arr[] = new String[2];
		arr[0] = jwtService.generateAccessToken(user);
		arr[1] = jwtService.generateRefreshToken(user);
		
		return arr;
		
	}
	
	public String refreshToken(String refreshToken) {
		Long id = jwtService.getUserIdFromToken(refreshToken);
		
		User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User Not Found With Id: " + id));
		
		return jwtService.generateAccessToken(user);
	
	}
 	
	
}
