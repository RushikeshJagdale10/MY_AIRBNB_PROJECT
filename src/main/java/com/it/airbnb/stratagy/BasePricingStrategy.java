package com.it.airbnb.stratagy;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.it.airbnb.entity.Inventory;


public class BasePricingStrategy implements PricingStrategy {

	@Override
	public BigDecimal calculatePrice(Inventory inventory) {
		
		return inventory.getRoom().getBasePrice();
	}

}
