package com.it.airbnb.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.it.airbnb.dto.BookingDto;
import com.it.airbnb.dto.BookingRequest;
import com.it.airbnb.dto.GuestDto;

@Service
public interface BookingService {

	BookingDto initialiseBooking(BookingRequest bookingRequest);

	BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList);

}
