package com.it.airbnb.util;

import org.springframework.security.core.context.SecurityContextHolder;

import com.it.airbnb.entity.User;

public class AppUtils {
	
	public static User getCurrentUser() {
		
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

}
