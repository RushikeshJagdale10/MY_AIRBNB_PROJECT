package com.it.airbnb.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.it.airbnb.dto.HotelDto;
import com.it.airbnb.dto.HotelInfoDto;
import com.it.airbnb.dto.RoomDto;
import com.it.airbnb.entity.Hotel;
import com.it.airbnb.entity.Room;
import com.it.airbnb.exception.ResourceNotFoundException;
import com.it.airbnb.repository.HotelRepository;
import com.it.airbnb.repository.RoomRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

	
	private final HotelRepository hotelRepository;
	private final InventoryService inventoryService;
	private final RoomRepository roomRepository;
	private final ModelMapper modelMapper;
	
	@Override
	public HotelDto createNewHotel(HotelDto hotelDto) {
		log.info("Creating a new hotel with name: {}", hotelDto.getName());
		log.info("HotelContactInfo in DTO before mapping: {}", hotelDto.getHotelContactInfo());
		Hotel hotel = modelMapper.map(hotelDto, Hotel.class);
		hotel.setActive(false);
		hotel = hotelRepository.save(hotel);
		log.info("Created a new Hotel with ID: {}" + hotel.getId());
		log.info("Saved Hotel with ID: {}, HotelContactInfo: {}", hotel.getId(), hotel.getHotelContactInfo());
		return modelMapper.map(hotel, HotelDto.class);
	}
	
	@Override
	public List<HotelDto> getAllHotels() {
		
		List<HotelDto> hotels = hotelRepository.findAllHotels();
		if(hotels.isEmpty()) throw new ResourceNotFoundException("Hotels Not found...");
		
		log.info("Hotels Data : " + hotels);
		
		// Convert List<Hotel> to List<HotelDto>
	    return hotels;

	}
	
	

	@Override
	public HotelDto getHotelById(Long id) {
		log.info("Getting the Hotel with Id: " + id);
		Hotel hotel = hotelRepository
						.findById(id)
						.orElseThrow(() -> new ResourceNotFoundException("Hotel not found with Id: " + id));
	
		return modelMapper.map(hotel, HotelDto.class);
	
	}

	@Override
	public HotelDto updateHotelById(Long id, HotelDto hotelDto) {
		log.info("Updating the Hotel with Id: ", id);
		Hotel hotel = hotelRepository
						.findById(id)
						.orElseThrow(() -> new ResourceNotFoundException("Hotel not found with Id: " + id));
		
		modelMapper.map(hotelDto, hotel);
		hotel.setId(id);
		hotelRepository.save(hotel);
		
		return modelMapper.map(hotel, HotelDto.class);
	}

	@Override
	@Transactional
	public void deleteHotelById(Long id) {
		
		//for future deletion of inventories we need hotel data
		
		log.info("Deleting the Hotel with Id: ", id);
		Hotel hotel = hotelRepository
				.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Hotel not found with Id: " + id));

		
		//TODO: delete the future inventories for this hotel
		for(Room room : hotel.getRooms()) {
		inventoryService.deleteAllInventories(room);
		roomRepository.deleteById(room.getId());
		}
		hotelRepository.deleteById(id);
		
		log.info("Deleted the Hotel with Id: ", id);
	}

	@Override
	@Transactional
	public void activateHotel(Long hotelId) {
		log.info("Activating the Hotel with Id: "+ hotelId);
		Hotel hotel = hotelRepository
						.findById(hotelId)
						.orElseThrow(() -> new ResourceNotFoundException("Hotel not found with Id: " + hotelId));
		hotel.setActive(true);
		log.info("Testing for next steps..");
		//Assuming only do it once
		for(Room room : hotel.getRooms()) {
			log.info("Initializing Room Availability for year inside Inventory with Room Id: " + room.getId());
			inventoryService.initializeRoomForAYear(room);
		}
	}

	@Override
	public HotelInfoDto getHotelInfoById(Long hotelId) {
		log.info("Get Hotel Info by id {}", hotelId);
		Hotel hotel = hotelRepository
				.findById(hotelId)
				.orElseThrow(() -> new ResourceNotFoundException("Hotel not found with Id: " + hotelId));
		
		List<RoomDto> rooms = hotel.getRooms()
									.stream()
									.map(element -> modelMapper.map(element, RoomDto.class))
									.toList();
		
		
		return new HotelInfoDto(modelMapper.map(hotel, HotelDto.class), rooms);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
