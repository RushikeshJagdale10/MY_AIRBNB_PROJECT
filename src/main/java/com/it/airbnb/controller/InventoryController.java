package com.it.airbnb.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.it.airbnb.dto.InventoryDto;
import com.it.airbnb.dto.UpdateInventoryRequestDto;
import com.it.airbnb.service.InventoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/inventory")
public class InventoryController {
	
	
	private final InventoryService inventoryService;
	
	@GetMapping("/rooms/{roomId}")
	public List<InventoryDto> getAllInventoryByRoom(@PathVariable Long roomId) {
		
		return inventoryService.getAllInventoryByRoom(roomId);
		
		
	}
	
	@PatchMapping("/rooms/{roomId}")
	public ResponseEntity<Void> updateInventory(@PathVariable Long roomId, 
											@RequestBody UpdateInventoryRequestDto updateInventoryRequestDto) {
		
		
		inventoryService.updateInventory(roomId, updateInventoryRequestDto);
		
		return ResponseEntity.noContent().build();
	}

}
