package com.it.airbnb.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.it.airbnb.dto.BookingDto;
import com.it.airbnb.dto.BookingRequest;
import com.it.airbnb.dto.GuestDto;
import com.it.airbnb.service.BookingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/bookings")
public class HotelBookingController {

	private final BookingService bookingService;
	
	@PostMapping("/init")
	public ResponseEntity<BookingDto> initialiseBooking(@RequestBody BookingRequest bookingRequest) {
		
		return ResponseEntity.ok(bookingService.initialiseBooking(bookingRequest));
		
	}
	
	@PostMapping("/{bookingId}/addGuests")
	public ResponseEntity<BookingDto> addGuests(@PathVariable Long bookingId,
												@RequestBody List<GuestDto> guestDtoList) {
		
		
		return ResponseEntity.ok(bookingService.addGuests(bookingId, guestDtoList));
	}
	
	
}
