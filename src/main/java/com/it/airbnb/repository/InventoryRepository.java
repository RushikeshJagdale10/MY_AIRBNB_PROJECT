package com.it.airbnb.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.it.airbnb.entity.Inventory;
import com.it.airbnb.entity.Room;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long>{
	
	
	void deleteByDateAfterAndRoom(LocalDate date, Room room);
	
}
