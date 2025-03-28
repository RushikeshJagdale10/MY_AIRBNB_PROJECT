package com.it.airbnb.dto;

import com.it.airbnb.entity.User;
import com.it.airbnb.enums.Gender;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuestDto {
	
	private Long id;
	private User user;
	private String name;
	private Gender gender;
	private Integer age;

}
