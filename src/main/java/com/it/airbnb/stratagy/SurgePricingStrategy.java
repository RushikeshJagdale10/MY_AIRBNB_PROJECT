package com.it.airbnb.stratagy;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.it.airbnb.entity.Inventory;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class SurgePricingStrategy implements PricingStrategy {

	private final PricingStrategy wrapped;
	
	@Override
	public BigDecimal calculatePrice(Inventory inventory) {
		
		BigDecimal price =  wrapped.calculatePrice(inventory).multiply(inventory.getSurgeFactor());
	
		return price;
	}

}
