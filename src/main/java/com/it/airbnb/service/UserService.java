package com.it.airbnb.service;

import org.springframework.stereotype.Service;

import com.it.airbnb.entity.User;

@Service
public interface UserService {
	
	User getUserById(Long id);

}
