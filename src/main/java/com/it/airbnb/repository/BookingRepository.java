package com.it.airbnb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.it.airbnb.entity.Booking;
import com.it.airbnb.entity.Hotel;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

	Optional<Booking> findByPaymentSessionId(String sessionId);

}
