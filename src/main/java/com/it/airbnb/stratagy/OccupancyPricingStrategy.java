package com.it.airbnb.stratagy;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.it.airbnb.entity.Inventory;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class OccupancyPricingStrategy implements PricingStrategy {

	private final PricingStrategy wrapped;
	
	@Override
	public BigDecimal calculatePrice(Inventory inventory) {
		
		BigDecimal price = wrapped.calculatePrice(inventory);
		
		double occupancyRate = (double) inventory.getBookedCount() / inventory.getTotalCount();
		
		if(occupancyRate > 0.8) {
			
			price = price.multiply(BigDecimal.valueOf(1.2)); 
		}
		
		return price;
	}

	
	
	
}
