package com.it.airbnb.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.it.airbnb.dto.BookingDto;
import com.it.airbnb.dto.BookingRequest;
import com.it.airbnb.dto.GuestDto;
import com.it.airbnb.dto.HotelReportDto;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;

@Service
public interface BookingService {

	BookingDto initialiseBooking(BookingRequest bookingRequest);

	BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList);

	String initiatePayments(Long bookingId);

	void capturePayment(Event event);

	void cancelBooking(Long bookingId);

	String getBookingStatus(Long bookingId);

	List<BookingDto> getAllBookingsByHotelId(Long hotelId);

	HotelReportDto getHotelReport(Long hotelId, LocalDate startDate, LocalDate endDate);

	List<BookingDto> getMyBookings();

}
