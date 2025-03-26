package com.it.airbnb.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.it.airbnb.dto.RoomDto;

@Service
public interface RoomService {
	
	RoomDto createNewRoom(Long hotelId, RoomDto roomDto);
	
	List<RoomDto> getAllRoomsInHotel(Long hotelId);

	RoomDto getRoomById(Long roomId);
	
	void deleteRoomById(Long roomId);
	
	
}
