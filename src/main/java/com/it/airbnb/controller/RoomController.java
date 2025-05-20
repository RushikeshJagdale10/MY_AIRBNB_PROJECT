package com.it.airbnb.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.it.airbnb.dto.RoomDto;
import com.it.airbnb.service.RoomService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/hotels/{hotelId}/rooms")
public class RoomController {
	
	private final RoomService roomService;
	
	@PostMapping
	public ResponseEntity<RoomDto> createNewRoom(@PathVariable Long hotelId, @RequestBody RoomDto roomDto) {
		
		RoomDto room = roomService.createNewRoom(hotelId, roomDto);
		
		return new ResponseEntity<>(room, HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<List<RoomDto>> getAllRoomsInHotel(@PathVariable Long hotelId) {
		return ResponseEntity.ok(roomService.getAllRoomsInHotel(hotelId));
	}
	
	@GetMapping("/{roomId}")
	public ResponseEntity<RoomDto> getRoomById(@PathVariable Long hotelId, @PathVariable Long roomId) {
		log.info("Getting the room with Room Id: {}", roomId );
		return ResponseEntity.ok(roomService.getRoomById(roomId));
	}
	
	@DeleteMapping("/{roomId}")
	public ResponseEntity<RoomDto> deleteRoomById(@PathVariable Long hotelId, @PathVariable Long roomId) {
		roomService.deleteRoomById(roomId);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/{roomId}")
	public ResponseEntity<RoomDto> updateRoomById(@PathVariable Long hotelId, @PathVariable Long roomId, @RequestBody RoomDto roomDto) {
		
		
		
		return ResponseEntity.ok(roomService.updateRoomById(hotelId, roomId, roomDto));
		
	}
	
	
}
