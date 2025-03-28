package com.it.airbnb.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.it.airbnb.dto.HotelDto;
import com.it.airbnb.dto.HotelInfoDto;
import com.it.airbnb.dto.HotelPriceDto;
import com.it.airbnb.dto.HotelSearchRequest;
import com.it.airbnb.service.HotelService;
import com.it.airbnb.service.InventoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/hotels")
@Slf4j
@RequiredArgsConstructor
public class HotelBrowserController {
	
	private final InventoryService inventoryService;
	private final HotelService hotelService;
	
	@GetMapping("/search")
    public ResponseEntity<Page<HotelPriceDto>> searchHotels(@RequestBody HotelSearchRequest hotelSearchRequest) {

        Page<HotelPriceDto> page = inventoryService.searchHotels(hotelSearchRequest);
        return ResponseEntity.ok(page);
    }
	
	@GetMapping("/{hotelId}/info")
	public ResponseEntity<HotelInfoDto> getHotelInfo(@PathVariable Long hotelId) {
		
		
		return ResponseEntity.ok(hotelService.getHotelInfoById(hotelId));
	}

}
