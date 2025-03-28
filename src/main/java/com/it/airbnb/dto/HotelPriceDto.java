package com.it.airbnb.dto;

import com.it.airbnb.entity.Hotel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelPriceDto {
	
	private Hotel hotel;
	
	private Double price; 

}
