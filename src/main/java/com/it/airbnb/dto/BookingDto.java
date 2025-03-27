package com.it.airbnb.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import com.it.airbnb.entity.Hotel;
import com.it.airbnb.entity.Room;
import com.it.airbnb.entity.User;
import com.it.airbnb.enums.BookingStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
	
	private Long id;
	
	private Hotel hotel;
	
	private Room room;
	
	private User user;
	
	private Integer roomCounts;
	
	private LocalDate checkInDate;
	
	private LocalDate checkOutDate;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;
	
	private BookingStatus bookingStatus;
	
	private Set<GuestDto> guests;

}
