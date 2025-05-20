package com.it.airbnb.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelReportDto {
	
	private Long bookingCount;
	
	private BigDecimal totalRevenue;
	
	private BigDecimal avgRevenue;
	
}
