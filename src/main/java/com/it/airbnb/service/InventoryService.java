package com.it.airbnb.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.it.airbnb.dto.HotelDto;
import com.it.airbnb.dto.HotelPriceDto;
import com.it.airbnb.dto.HotelSearchRequest;
import com.it.airbnb.entity.Room;

@Service
public interface InventoryService {
	
	void initializeRoomForAYear(Room room);
	
	void deleteAllInventories(Room room);

	Page<HotelPriceDto> searchHotels(HotelSearchRequest hotelSearchRequest);

}
