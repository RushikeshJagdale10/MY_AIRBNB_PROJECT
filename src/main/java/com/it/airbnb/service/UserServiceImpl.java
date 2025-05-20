package com.it.airbnb.service;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.it.airbnb.dto.ProfileUpdateRequestDto;
import com.it.airbnb.dto.UserDto;
import com.it.airbnb.entity.User;
import com.it.airbnb.exception.ResourceNotFoundException;
import com.it.airbnb.repository.UserRepository;
import com.it.airbnb.util.AppUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService{
	
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;
	
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

	@Override
	public void updateProfile(ProfileUpdateRequestDto profileUpdateRequestDto) {
		// TODO Auto-generated method stub
		
		User user = AppUtils.getCurrentUser();
		
		if(profileUpdateRequestDto.getDateOfBirth() != null ) user.setDateOfBirth(profileUpdateRequestDto.getDateOfBirth());
		if(profileUpdateRequestDto.getGender() != null ) user.setGender(profileUpdateRequestDto.getGender());
		if(profileUpdateRequestDto.getName() != null ) user.setName(profileUpdateRequestDto.getName());
		
		userRepository.save(user);
		
	}

	@Override
	public UserDto getMyProfile() {
		
		User user = AppUtils.getCurrentUser();
		
		log.info("User Profile With Id: {}", user.getId());
		
		return modelMapper.map(user, UserDto.class);
	}

}
