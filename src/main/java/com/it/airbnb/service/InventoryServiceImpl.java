package com.it.airbnb.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.it.airbnb.dto.HotelDto;
import com.it.airbnb.dto.HotelSearchRequest;
import com.it.airbnb.entity.Hotel;
import com.it.airbnb.entity.Inventory;
import com.it.airbnb.entity.Room;
import com.it.airbnb.exception.ResourceNotFoundException;
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
                    .reservedCount(0)
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
	public void deleteAllInventories(Room room) {
		log.info("Deleting the inventories of room with id: {}", room.getId());
		inventoryRepository.deleteByRoom(room);
		
	}


	@Override
    public Page<HotelDto> searchHotels(HotelSearchRequest hotelSearchRequest) {
        log.info("Searching hotels for {} city, from {} to {}", hotelSearchRequest.getCity(), hotelSearchRequest.getStartDate(), hotelSearchRequest.getEndDate());
        Pageable pageable = PageRequest.of(hotelSearchRequest.getPage(), hotelSearchRequest.getSize());
        long dateCount =
                ChronoUnit.DAYS.between(hotelSearchRequest.getStartDate(), hotelSearchRequest.getEndDate()) + 1;

        Page<Hotel> hotelPage = inventoryRepository.findHotelsWithAvailableInventory(hotelSearchRequest.getCity(),
                hotelSearchRequest.getStartDate(), hotelSearchRequest.getEndDate(), hotelSearchRequest.getRoomsCount(),
                dateCount, pageable);
        
        if(hotelPage.isEmpty()) throw new ResourceNotFoundException("Hotels Mentioned Between Dates are Not Available!!");

        return hotelPage.map((element) -> modelMapper.map(element, HotelDto.class));
    }
	
	

}
