package com.it.airbnb.stratagy;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.it.airbnb.entity.Inventory;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class HolidayPricingStrategy implements PricingStrategy {

	private final PricingStrategy wrapped;
	
	@Override
	public BigDecimal calculatePrice(Inventory inventory) {
		
		BigDecimal price = wrapped.calculatePrice(inventory);
		
		boolean isTodayHoliday = true; //TODO: call an api or check with local Date
		
		if(isTodayHoliday) {
			
			price = price.multiply(BigDecimal.valueOf(1.25));
		}
		
		return price;
	}

}
