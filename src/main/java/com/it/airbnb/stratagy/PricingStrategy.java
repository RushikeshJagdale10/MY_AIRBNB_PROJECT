package com.it.airbnb.stratagy;

import java.math.BigDecimal;



import com.it.airbnb.entity.Inventory;



public interface PricingStrategy {
	
	
	BigDecimal calculatePrice(Inventory inventory);

}
