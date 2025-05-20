package com.it.airbnb.dto;

import java.math.BigDecimal;
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
public class BookingDto {
	
	private Long id;
	
	private Integer roomCounts;
	
	private LocalDate checkInDate;
	
	private LocalDate checkOutDate;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;
	
	private BookingStatus bookingStatus;
	
	private Set<GuestDto> guests;
	
	private BigDecimal amount;

}
