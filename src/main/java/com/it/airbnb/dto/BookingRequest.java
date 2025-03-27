package com.it.airbnb.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {
	
	private Long hotelId;
	
	private Long roomId;
	
	private LocalDate checkInDate;
	
	private LocalDate checkOutDate;
	
	private Integer roomsCount;

}
