package com.it.airbnb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.it.airbnb.entity.Hotel;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

}
