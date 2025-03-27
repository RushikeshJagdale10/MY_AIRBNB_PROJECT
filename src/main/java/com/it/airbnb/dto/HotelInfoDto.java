package com.it.airbnb.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelInfoDto {
	
	private HotelDto hotel;
	
	private List<RoomDto> rooms;

}
