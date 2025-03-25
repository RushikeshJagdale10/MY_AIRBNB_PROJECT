package com.it.airbnb.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.it.airbnb.dto.HotelDto;
import com.it.airbnb.entity.Hotel;
import com.it.airbnb.exception.ResourceNotFoundException;
import com.it.airbnb.repository.HotelRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

	
	private final HotelRepository hotelRepository;
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
	public HotelDto getHotelById(Long id) {
		log.info("Getting the Hotel with Id: ", id);
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
	public Boolean deleteHotelById(Long id) {
		log.info("Deleting the Hotel with Id: ", id);
		boolean exists = hotelRepository.existsById(id);
		if(!exists) throw new ResourceNotFoundException("Hotel not found with Id: " + id);
		
		hotelRepository.deleteById(id);
		//TODO: delete the future inventories for this hotel
		
		return true;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
