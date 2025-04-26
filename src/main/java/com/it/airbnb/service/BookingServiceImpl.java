package com.it.airbnb.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
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
import com.it.airbnb.stratagy.PricingService;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.param.RefundCreateParams;

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
	private final CheckOutService checkOutService;
	private final PricingService pricingService;
	
	@Value("${frontend.url}")
	private String frontEndUrl;
	
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
		inventoryRepository.initBooking(room.getId(), bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate(), bookingRequest.getRoomsCount());
		
		
		
		//TODO: Calculate Dynamic amount
		BigDecimal priceForOneRoom = pricingService.calculateTotalPrice(inventoryList);
		BigDecimal totalPrice = priceForOneRoom.multiply(BigDecimal.valueOf(bookingRequest.getRoomsCount()));
		
		Booking booking = Booking.builder()
								 .bookingStatus(BookingStatus.RESERVED)
								 .hotel(hotel)
								 .room(room)
								 .checkInDate(bookingRequest.getCheckInDate())
								 .checkOutDate(bookingRequest.getCheckOutDate())
								 .user(getCurrentUser())
								 .roomCounts(bookingRequest.getRoomsCount())
								 .amount(totalPrice)
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
	
	@Override
	@Transactional
	public String initiatePayments(Long bookingId) {
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(()-> new ResourceNotFoundException("Booking Not Found With Id: " + bookingId));
		
		//TODO: Get Current User from Security Context Holder
		User user = getCurrentUser();
		
		if(!user.equals(booking.getUser())) {
			throw new UnAuthorisedException("Booking does not belong to this user with Id: " + user.getId());
			
		}
		
		if(hasBookingExpired(booking)) {
			throw new IllegalStateException("Booking has Already Expired!!");
		}
				
		String sessionUrl = checkOutService.getCheckOutSession(booking, frontEndUrl+"/payments/success", frontEndUrl+"/payments/failure");
		
		booking.setBookingStatus(BookingStatus.PAYMENTS_PENDING);
		bookingRepository.save(booking);
		
		return sessionUrl;
	}
	
	@Override
    @Transactional
    public void capturePayment(Event event) {
        // Only handle "checkout.session.completed" events
        if (!"checkout.session.completed".equals(event.getType())) {
            log.warn("Received unsupported event type: {}", event.getType());
            return;
        }

        // Extract the session object from the event data
        Session session = (Session) event.getData().getObject();
        if (session == null) {
            log.error("Session data is missing in the event");
            return;
        }

        String sessionId = session.getId();
        Optional<Booking> bookingOpt = bookingRepository.findByPaymentSessionId(sessionId);

        // If the booking is not found for the given sessionId, log and return
        if (bookingOpt.isEmpty()) {
            log.warn("Booking not found for sessionId: {}", sessionId);
            return;
        }

        Booking booking = bookingOpt.get();

        // If the booking is already confirmed, no further action is needed
        if (booking.getBookingStatus() == BookingStatus.CONFIRMED) {
            log.info("Booking already confirmed for bookingId: {}", booking.getId());
            return;
        }

        // Confirm the booking and save it
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);

        // Perform inventory locking and booking confirmation actions
        inventoryRepository.findAndLockReservedInventory(
                booking.getRoom().getId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getRoomCounts()
        );

        inventoryRepository.confirmBooking(
                booking.getRoom().getId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getRoomCounts()
        );

        log.info("Booking confirmed successfully for bookingId: {}", booking.getId());
    }
	
	@Override
	@Transactional
	public void cancelBooking(Long bookingId) {
		
		log.info("Cancel Booking Started With Booking Id: {}", bookingId );
		
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(()-> new ResourceNotFoundException("Booking Not Found With Id: " + bookingId));
		
		//TODO: Get Current User from Security Context Holder
		User user = getCurrentUser();
		
		if(!user.equals(booking.getUser())) {
			throw new UnAuthorisedException("Booking does not belong to this user with Id: " + user.getId());
			
		}
		
		if(booking.getBookingStatus() != BookingStatus.CONFIRMED) {
			
			throw new IllegalStateException("Only Confirmed Bookings Can be Cancelled!!!");
			
		}
		
		booking.setBookingStatus(BookingStatus.CANCELLED);
		bookingRepository.save(booking);
		
		// Perform inventory locking and booking confirmation actions
        inventoryRepository.findAndLockReservedInventory(
                booking.getRoom().getId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getRoomCounts()
        );
        
        // Perform cancel Booking operation and changing inventory details
        inventoryRepository.cancelBooking(
        		booking.getRoom().getId(), 
        		booking.getCheckInDate(), 
        		booking.getCheckOutDate(), 
        		booking.getRoomCounts());
        
        //TODO: Handle The Refund Process
        
        try {
        	
        	Session session = Session.retrieve(booking.getPaymentSessionId());
        
        	RefundCreateParams refundParams = RefundCreateParams.builder()
        			.setPaymentIntent(session.getPaymentIntent())
        			.build();
        	
        	Refund.create(refundParams);
        
        } catch(StripeException ex) {
        	
        	throw new RuntimeException(ex);
        	
        }
		
	}
	
	
	@Override
	public String getBookingStatus(Long bookingId) {
		
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(()-> new ResourceNotFoundException("Booking Not Found With Id: " + bookingId));
		
		//TODO: Get Current User from Security Context Holder
		User user = getCurrentUser();
		
		if(!user.equals(booking.getUser())) {
			throw new UnAuthorisedException("Booking does not belong to this user with Id: " + user.getId());
			
		}
		
		return booking.getBookingStatus().name();
		
	}



	
	public boolean hasBookingExpired(Booking booking) {
		
		return booking.getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now());
		
	}
	
	public User getCurrentUser() {
		
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	

	

	

	
	
	

}
