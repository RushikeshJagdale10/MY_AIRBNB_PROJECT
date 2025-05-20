package com.it.airbnb.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.it.airbnb.dto.BookingDto;
import com.it.airbnb.entity.Booking;
import com.it.airbnb.entity.Hotel;
import com.it.airbnb.entity.User;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

	Optional<Booking> findByPaymentSessionId(String sessionId);

	List<Booking> findByHotel(Hotel hotel);

	List<Booking> findByHotelAndCreatedAtBetween(Hotel hotel, LocalDateTime startDateTime, LocalDateTime endDateTime);

	List<Booking> findByUser(User user);
	
}
