package com.it.airbnb.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.it.airbnb.dto.BookingDto;
import com.it.airbnb.dto.ProfileUpdateRequestDto;
import com.it.airbnb.dto.UserDto;
import com.it.airbnb.entity.Booking;
import com.it.airbnb.entity.User;
import com.it.airbnb.service.BookingService;
import com.it.airbnb.service.UserService;
import com.it.airbnb.util.AppUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
	
	private final UserService userService;
	
	private final BookingService bookingService;
	
	
	@GetMapping("/profile")
	public ResponseEntity<UserDto> getMyProfile() {
		
		return ResponseEntity.ok(userService.getMyProfile());
		
	}
	
	
	@PatchMapping("/profile")
	public ResponseEntity<Void> updateProfile(@RequestBody ProfileUpdateRequestDto profileUpdateRequestDto) {
		
		userService.updateProfile(profileUpdateRequestDto);
		
		return ResponseEntity.noContent().build();
		
	}
	
	
	
	@GetMapping("/myBookings")
	public ResponseEntity<List<BookingDto>> getMyBookings() {
	
		return ResponseEntity.ok(bookingService.getMyBookings());	
	}
	
	

}
