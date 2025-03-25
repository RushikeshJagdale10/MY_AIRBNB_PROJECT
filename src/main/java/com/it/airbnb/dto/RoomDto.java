package com.it.airbnb.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDto {

	private Long id;
	
	private String type;
	
	private BigDecimal basePrice;
	
	private String[] photos;
	
	private String[] amenities;	
	
	private Integer totalCount;
	
	private Integer capacity;
	

	
}
