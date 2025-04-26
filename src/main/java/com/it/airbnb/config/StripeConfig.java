package com.it.airbnb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.stripe.Stripe;

@Configuration
public class StripeConfig {
	
	public StripeConfig(@Value("${stripe.secret.key}") String stripeSecretKey) {
		
		Stripe.apiKey = stripeSecretKey;
		
	}

}
