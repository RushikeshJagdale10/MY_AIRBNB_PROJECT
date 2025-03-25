package com.it.airbnb.dto;

import com.it.airbnb.entity.HotelContactInfo;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelDto {
	
	private Long id;
	
	private String name;
	
	private String city;

	private String[] photos;
	
	private String[] amenities;	
		
	private HotelContactInfo hotelContactInfo;
	
	private Boolean active;
	
	
	
}
