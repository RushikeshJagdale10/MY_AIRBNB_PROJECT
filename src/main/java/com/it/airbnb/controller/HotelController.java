package com.it.airbnb.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.it.airbnb.dto.BookingDto;
import com.it.airbnb.dto.HotelDto;
import com.it.airbnb.dto.HotelReportDto;
import com.it.airbnb.service.BookingService;
import com.it.airbnb.service.HotelService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/admin/hotels")
@RequiredArgsConstructor
@Slf4j
public class HotelController {
	
	private final HotelService hotelService;
	private final BookingService bookingService;
	
	@PostMapping
	public ResponseEntity<HotelDto> createNewHotel(@RequestBody HotelDto hotelDto) {
		
		log.info("Attempting to create new Hotel with name: " + hotelDto.getName());
		
		HotelDto hotel = hotelService.createNewHotel(hotelDto);
		
		return new ResponseEntity<>(hotel, HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<List<HotelDto>> getAllHotels() {
		
		log.info("Attempting to get all hotels. ");
		
		List<HotelDto> hotel = hotelService.getAllHotels();
		
		return new ResponseEntity<>(hotel, HttpStatus.OK);
	}
	
	
	
	
	@GetMapping("/{hotelId}")
	public ResponseEntity<HotelDto> getHotelById(@PathVariable Long hotelId) {
	
		HotelDto hotel = hotelService.getHotelById(hotelId);
		
		return new ResponseEntity<>(hotel, HttpStatus.CREATED);
	}
	
	@PutMapping("/{hotelId}")
	public ResponseEntity<HotelDto> updateHotelById(@PathVariable Long hotelId, @RequestBody HotelDto hotelDto ) {
		
		HotelDto hotel = hotelService.updateHotelById(hotelId, hotelDto);
		
		return ResponseEntity.ok(hotel);
	}
	
	@DeleteMapping("/{hotelId}") 
	public ResponseEntity<Void> delteHotelById(@PathVariable Long hotelId) {
		
		hotelService.deleteHotelById(hotelId);
		
		return ResponseEntity.noContent().build();
	}
	
	@PatchMapping("/{hotelId}") 
	public ResponseEntity<Void> activateHotelById(@PathVariable Long hotelId) {
	
		hotelService.activateHotel(hotelId);
		
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{hotelId}/bookings")
	public ResponseEntity<List<BookingDto>> getAllBookingsByHotelId(@PathVariable Long hotelId) {
		
		return ResponseEntity.ok(bookingService.getAllBookingsByHotelId(hotelId));
	}
	
	@GetMapping("/{hotelId}/reports")
	public ResponseEntity<HotelReportDto> getHotelReport(@PathVariable Long hotelId, 
														 @RequestParam(required = false) LocalDate startDate,
														 @RequestParam(required = false) LocalDate endDate) {
		
		if(startDate == null) startDate = LocalDate.now().minusMonths(1);
		if(endDate == null) endDate = LocalDate.now();
		
		return ResponseEntity.ok(bookingService.getHotelReport(hotelId, startDate, endDate));
	}

	
}
