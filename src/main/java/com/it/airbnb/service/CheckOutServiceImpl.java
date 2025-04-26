package com.it.airbnb.service;

import java.math.BigDecimal;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.it.airbnb.entity.Booking;
import com.it.airbnb.entity.User;
import com.it.airbnb.repository.BookingRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.checkout.SessionCreateParams;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class CheckOutServiceImpl implements CheckOutService {
	
	private final BookingRepository bookingRepository;
	
	
	@Override
	public String getCheckOutSession(Booking booking, String successUrl, String failureUrl) {
		
		log.info("Creating Session for Booking With Id: {}", booking.getId());
		
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		try {
			
				CustomerCreateParams customerCreateParams = CustomerCreateParams.builder()
						.setName(user.getName())
						.setEmail(user.getEmail())
						.build();
						
				Customer customer = Customer.create(customerCreateParams);
				
				SessionCreateParams sessionParams = SessionCreateParams.builder()
						.setMode(SessionCreateParams.Mode.PAYMENT)
						.setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
						.setCustomer(customer.getId())
						.setSuccessUrl(successUrl)
						.setCancelUrl(failureUrl)
						.addLineItem(
								SessionCreateParams.LineItem.builder()
										.setQuantity(1L)
										.setPriceData(
												SessionCreateParams.LineItem.PriceData.builder()
														.setCurrency("inr")
														.setUnitAmount(booking.getAmount().multiply(BigDecimal.valueOf(100)).longValue())
														.setProductData(
																SessionCreateParams.LineItem.PriceData.ProductData.builder()
																		.setName(booking.getHotel().getName() + " : " + booking.getRoom().getType())
																		.setDescription("Booking Id: " + booking.getId())
																		.build()
																)
														.build()
												)
										.build()
						)
						.build();
			
				Session session = Session.create(sessionParams);
			
				booking.setPaymentSessionId(session.getId());
				bookingRepository.save(booking);
				
				log.info("Session Created Successfully For Booking Id: {}", booking.getId());
				
				return session.getUrl();
				
		} catch(StripeException ex) {
			throw new RuntimeException(ex);
			
		}
	
	}

}
