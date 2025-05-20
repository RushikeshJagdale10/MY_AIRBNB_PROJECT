package com.it.airbnb.service;

import org.springframework.stereotype.Service;

import com.it.airbnb.dto.ProfileUpdateRequestDto;
import com.it.airbnb.dto.UserDto;
import com.it.airbnb.entity.User;

@Service
public interface UserService {
	
	User getUserById(Long id);

	void updateProfile(ProfileUpdateRequestDto profileUpdateRequestDto);

	UserDto getMyProfile();

}
