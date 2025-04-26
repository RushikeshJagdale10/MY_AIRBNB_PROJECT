package com.it.airbnb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelSearchDto {
	
	private Long id;
	
	private String name;
	
	private String city;
	
	private Boolean active;

}
