package com.it.airbnb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.it.airbnb.entity.Inventory;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long>{

}
