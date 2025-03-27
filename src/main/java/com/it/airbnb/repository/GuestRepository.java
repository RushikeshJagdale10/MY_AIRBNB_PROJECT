package com.it.airbnb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.it.airbnb.entity.Guest;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long>{

}
