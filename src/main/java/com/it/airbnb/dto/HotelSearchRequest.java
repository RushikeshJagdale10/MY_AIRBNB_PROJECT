package com.it.airbnb.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelSearchRequest {
	
	private String city;
	
	private LocalDate startDate;
	
	private LocalDate endDate;
	
	private Integer roomsCount;
	
	private Integer page=0;
	
	private Integer size=10;

}
