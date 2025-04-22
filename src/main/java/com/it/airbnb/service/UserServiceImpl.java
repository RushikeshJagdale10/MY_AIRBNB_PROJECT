package com.it.airbnb.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.it.airbnb.entity.User;
import com.it.airbnb.exception.ResourceNotFoundException;
import com.it.airbnb.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService{
	
	private final UserRepository userRepository;

	@Override
	public User getUserById(Long id) {
		
		User user = userRepository.findById(id)
								  .orElseThrow(() -> new ResourceNotFoundException("User Not FOund With User Id: " + id));
		
		return user;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		
		return userRepository.findByEmail(username)
				  .orElseThrow(() -> new ResourceNotFoundException("User Not Found With UserName: " + username));
		
	}

}
