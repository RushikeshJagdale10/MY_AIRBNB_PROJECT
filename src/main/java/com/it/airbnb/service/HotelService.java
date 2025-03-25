package com.it.airbnb.service;

import org.springframework.stereotype.Service;

import com.it.airbnb.dto.HotelDto;
import com.it.airbnb.entity.Hotel;

@Service
public interface HotelService {
	
	HotelDto createNewHotel(HotelDto hotelDto);
	
	HotelDto getHotelById(Long id);
	
	HotelDto updateHotelById(Long id, HotelDto hotelDto);
	
	Boolean deleteHotelById(Long id);

}
