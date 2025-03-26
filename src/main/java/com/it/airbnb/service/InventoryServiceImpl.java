package com.it.airbnb.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.it.airbnb.entity.Inventory;
import com.it.airbnb.entity.Room;
import com.it.airbnb.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService{
	
	private final InventoryRepository inventoryRepository;
	private final ModelMapper modelMapper;
	
	
	@Override
	@Transactional
	public void initializeRoomForAYear(Room room) {
		
		log.info("INSIDE initializeRoomForAYear Method : Initializing Room Availability for year inside Inventory with Room Id: " + room.getId());
		
		LocalDate today = LocalDate.now();
		log.info("INSIDE initializeRoomForAYear Method : today's Date :" + today);
		LocalDate endDate = today.plusYears(1);
        for (; !today.isAfter(endDate); today=today.plusDays(1)) {
            Inventory inventory = Inventory.builder()
                    .hotel(room.getHotel())
                    .room(room)
                    .bookedCount(0)
                    .city(room.getHotel().getCity())
                    .date(today)
                    .price(room.getBasePrice())
                    .surgeFactor(BigDecimal.ONE)
                    .totalCount(room.getTotalCount())
                    .closed(false)
                    .build();
            inventoryRepository.save(inventory);
        }
		
		log.info("INSIDE initializeRoomForAYear Method : Room Initialized for a Year...");
	}


	@Override
	public void deleteFutureInventories(Room room) {
		LocalDate today = LocalDate.now();
		inventoryRepository.deleteByDateAfterAndRoom(today, room);
		
	}
	
	

}
