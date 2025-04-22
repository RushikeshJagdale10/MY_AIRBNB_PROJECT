package com.it.airbnb.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.it.airbnb.dto.BookingDto;
import com.it.airbnb.dto.BookingRequest;
import com.it.airbnb.dto.GuestDto;
import com.it.airbnb.entity.Booking;
import com.it.airbnb.entity.Guest;
import com.it.airbnb.entity.Hotel;
import com.it.airbnb.entity.Inventory;
import com.it.airbnb.entity.Room;
import com.it.airbnb.entity.User;
import com.it.airbnb.enums.BookingStatus;
import com.it.airbnb.exception.ResourceNotFoundException;
import com.it.airbnb.exception.UnAuthorisedException;
import com.it.airbnb.repository.BookingRepository;
import com.it.airbnb.repository.GuestRepository;
import com.it.airbnb.repository.HotelRepository;
import com.it.airbnb.repository.InventoryRepository;
import com.it.airbnb.repository.RoomRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{

	private final BookingRepository bookingRepository;
	private final HotelRepository hotelRepository;
	private final RoomRepository roomRepository;
	private final ModelMapper modelMapper;
	private final InventoryRepository inventoryRepository;
	private final GuestRepository guestRepository;
	
	
	@Override
	@Transactional
	public BookingDto initialiseBooking(BookingRequest bookingRequest) {
		
		log.info("Initialising Booking for hotel : {}, room: {}, date {} - {}", 
									bookingRequest.getHotelId(),
									bookingRequest.getRoomId(),
									bookingRequest.getCheckInDate(),
									bookingRequest.getCheckOutDate());
		
		Hotel hotel = hotelRepository
						.findById(bookingRequest.getHotelId())
						.orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + bookingRequest.getHotelId()));
		
		Room room = roomRepository
						.findById(bookingRequest.getRoomId())
						.orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + bookingRequest.getRoomId()));
		
		boolean isRoomInsideHotelExists = hotel.getRooms()
											   .stream()
											   .anyMatch(rooms -> rooms.getId().equals(bookingRequest.getRoomId()));
		
		
		if(!isRoomInsideHotelExists) throw new ResourceNotFoundException("Inside Hotel with id: " + bookingRequest.getHotelId() + " Room Id: " + bookingRequest.getRoomId() + " is Not Available..");
		
		
		List<Inventory> inventoryList = inventoryRepository.findAndLockAvailableInventory(room.getId(),
				bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate(), bookingRequest.getRoomsCount());
		
		long daysCount = ChronoUnit.DAYS.between(bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate()) + 1;
		
		if(inventoryList.size() != daysCount) {
			throw new IllegalStateException("Room is Not Available AnyMore....");
		}
		
		//Reserve the rooms/ Update the Booked count of inventories
		for(Inventory inventory : inventoryList) {
			inventory.setReservedCount(inventory.getReservedCount() + bookingRequest.getRoomsCount());
		}
		
		inventoryRepository.saveAll(inventoryList);
		
		
		//TODO: Create the Booking
		
		User user = new User();
		//user.setId(1L); //TODO: REMOVE DUMMY USER
		
		//TODO: Calculate Dynamic amount
		
		Booking booking = Booking.builder()
								 .bookingStatus(BookingStatus.RESERVED)
								 .hotel(hotel)
								 .room(room)
								 .checkInDate(bookingRequest.getCheckInDate())
								 .checkOutDate(bookingRequest.getCheckOutDate())
								 .user(getCurrentUser())
								 .roomCounts(bookingRequest.getRoomsCount())
								 .amount(BigDecimal.TEN)
								 .build();
		
		booking = bookingRepository.save(booking);
		
		
		log.info("Booking for Hotel Id: {} and Room Id: {} is Done!!", bookingRequest.getHotelId(), bookingRequest.getRoomId());
		
		return modelMapper.map(booking, BookingDto.class);
		
	}

	@Override
	@Transactional
	public BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList) {
		
		log.info("Adding guests for booking wiht id: {}", bookingId);
		
		Booking booking = bookingRepository.findById(bookingId)
										   .orElseThrow(() -> new ResourceNotFoundException("Booking not found with Id: "+ bookingId));
		
		//TODO: Get Current User
		User user = getCurrentUser();
		
		if(!user.equals(booking.getUser())) {
			throw new UnAuthorisedException("Booking does not belong to this user with Id: " + user.getId());
			
		}
		
		if(hasBookingExpired(booking)) {
			throw new IllegalStateException("Booking has Already Expired!!");
		}
		
		if(booking.getBookingStatus() != BookingStatus.RESERVED) {
			throw new IllegalStateException("Booking Is Not Under Reserved State, Cannot Add Guests...");	
		}
		
		for(GuestDto guestDto : guestDtoList) {
			Guest guest = modelMapper.map(guestDto, Guest.class);
			guest.setUser(user);
			guest = guestRepository.save(guest);
			booking.getGuests().add(guest);
		}
		
		booking.setBookingStatus(BookingStatus.GUESTS_ADDED);
		bookingRepository.save(booking);
		
		return modelMapper.map(booking, BookingDto.class);
	}
	
	public boolean hasBookingExpired(Booking booking) {
		
		return booking.getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now());
		
	}
	
	public User getCurrentUser() {
		
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	

}
