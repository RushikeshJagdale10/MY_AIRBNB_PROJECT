package com.it.airbnb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.it.airbnb.dto.HotelDto;
import com.it.airbnb.entity.Hotel;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
	
	
	@Query("SELECT new com.it.airbnb.dto.HotelDto(h.id, h.name, h.city, h.photos, h.amenities, h.hotelContactInfo, h.active) FROM Hotel h")
    List<HotelDto> findAllHotels();
	
}
