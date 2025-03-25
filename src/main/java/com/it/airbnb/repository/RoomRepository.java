package com.it.airbnb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.it.airbnb.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

}
