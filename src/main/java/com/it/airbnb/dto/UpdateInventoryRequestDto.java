package com.it.airbnb.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateInventoryRequestDto {
	
	private LocalDate startDate;
	
	private LocalDate endDate;
	
	private BigDecimal surgeFactor;
	
	private Boolean closed;
	

}
