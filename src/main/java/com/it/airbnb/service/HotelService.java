package com.it.airbnb.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.it.airbnb.dto.HotelDto;
import com.it.airbnb.dto.HotelInfoDto;
import com.it.airbnb.entity.Hotel;

@Service
public interface HotelService {
	
	HotelDto createNewHotel(HotelDto hotelDto);
	
	HotelDto getHotelById(Long id);
	
	HotelDto updateHotelById(Long id, HotelDto hotelDto);
	
	void deleteHotelById(Long id);
	
	void activateHotel(Long id);

	List<HotelDto> getAllHotels();

	HotelInfoDto getHotelInfoById(Long hotelId);

}
