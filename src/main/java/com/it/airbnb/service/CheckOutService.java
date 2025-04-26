package com.it.airbnb.service;

import com.it.airbnb.entity.Booking;

public interface CheckOutService {
	
	
	String getCheckOutSession(Booking booking, String successUrl, String failureUrl);

}
