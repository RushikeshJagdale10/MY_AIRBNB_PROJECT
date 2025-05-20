package com.it.airbnb.dto;

import java.time.LocalDate;

import com.it.airbnb.enums.Gender;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileUpdateRequestDto {
	
	private String name;
	
	private LocalDate dateOfBirth;
	
	private Gender gender;

}
