package com.it.airbnb.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.it.airbnb.dto.RoomDto;
import com.it.airbnb.entity.Hotel;
import com.it.airbnb.entity.Room;
import com.it.airbnb.entity.User;
import com.it.airbnb.exception.ResourceNotFoundException;
import com.it.airbnb.exception.UnAuthorisedException;
import com.it.airbnb.repository.HotelRepository;
import com.it.airbnb.repository.RoomRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService {

	private final RoomRepository roomRepository;
	private final HotelRepository hotelRepository;
	private final InventoryService inventoryService;
	private final ModelMapper modelMapper;
	
	@Override
	public RoomDto createNewRoom(Long hotelId, RoomDto roomDto) {
		log.info("Creating a new room in hotel with Id: {}", hotelId );
		log.info("Getting Hotel data with Id: {}", hotelId );
		Hotel hotel = hotelRepository
				.findById(hotelId)
				.orElseThrow(() -> new ResourceNotFoundException("Hotel not found with Id: " + hotelId));
		
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(!user.equals(hotel.getOwner()))
		{
			throw new UnAuthorisedException("This User Does Not Own This Hotel With Id: " + hotelId);
		}
		
		
		Room room = modelMapper.map(roomDto, Room.class);
		room.setHotel(hotel);
		room = roomRepository.save(room);
		log.info("Room Details Saved with Id: {}", room.getId());
		
		//TODO : Create inventory as soon as room is created and if hotel is active
		if(hotel.getActive()) {
			inventoryService.initializeRoomForAYear(room);
		}
		
		
		return modelMapper.map(room, RoomDto.class);
	}
	
	
	
	@Override
	public List<RoomDto> getAllRoomsInHotel(Long hotelId) {
		log.info("Getting all rooms in Hotel with Id: {}", hotelId );
		Hotel hotel = hotelRepository
				.findById(hotelId)
				.orElseThrow(() -> new ResourceNotFoundException("Hotel not found with Id: " + hotelId));
		
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(!user.equals(hotel.getOwner()))
		{
			throw new UnAuthorisedException("This User Does Not Own This Hotel With Id: " + hotelId);
		}
		
		//Getting rooms from hotel and mapping into List<RoomDto>
		return hotel.getRooms()
					.stream()
					.map((element) -> modelMapper.map(element, RoomDto.class))
					.collect(Collectors.toList());
	}

	@Override
	public RoomDto getRoomById(Long roomId) {
		log.info("Getting the room with Room Id: {}", roomId );
		Room room = roomRepository.findById(roomId)
								  .orElseThrow(() -> new ResourceNotFoundException("Room not found with Id :" + roomId));
		return modelMapper.map(room, RoomDto.class);
	}

	@Override
	@Transactional
	public void deleteRoomById(Long roomId) {
		log.info("Deleting the room with Room Id: {}", roomId );
		Room room = roomRepository.findById(roomId)
				  .orElseThrow(() -> new ResourceNotFoundException("Room not found with Id :" + roomId));
		
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(!user.equals(room.getHotel().getOwner()))
		{
			throw new UnAuthorisedException("This User Does Not Own This room With Id: " + roomId);
		}
		
		
		//TODO: Delete all future room inventory for this room
		inventoryService.deleteAllInventories(room);
		roomRepository.deleteById(roomId);
		
		
	}

	

}
