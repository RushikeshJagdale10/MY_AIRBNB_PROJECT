package com.it.airbnb.stratagy;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.it.airbnb.entity.Inventory;

@Service
public class PricingService {
	
	public BigDecimal calculateDynamicPricing(Inventory inventory) {
		
		PricingStrategy pricingStrategy = new BasePricingStrategy();
		
		//Apply the Additional Strategies
		pricingStrategy = new SurgePricingStrategy(pricingStrategy);
		pricingStrategy = new OccupancyPricingStrategy(pricingStrategy);
		pricingStrategy = new HolidayPricingStrategy(pricingStrategy);
		pricingStrategy = new UrgencyPricingStrategy(pricingStrategy);
			
		return pricingStrategy.calculatePrice(inventory);
	}
	
	//Return the sum of price of this inventory list
	public BigDecimal calculateTotalPrice(List<Inventory> inventoryList) {
	
		BigDecimal totalPrice =	inventoryList.stream()
					.map(this::calculateDynamicPricing)
					.reduce(BigDecimal.ZERO, BigDecimal::add);
		
		return totalPrice;
		
	}

}
