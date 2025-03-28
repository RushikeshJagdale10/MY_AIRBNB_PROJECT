package com.it.airbnb.stratagy;

import java.math.BigDecimal;

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

}
