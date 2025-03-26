package com.it.airbnb.service;

import org.springframework.stereotype.Service;

import com.it.airbnb.entity.Room;

@Service
public interface InventoryService {
	
	void initializeRoomForAYear(Room room);
	
	void deleteFutureInventories(Room room);

}
