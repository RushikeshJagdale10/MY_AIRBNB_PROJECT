package com.it.airbnb.advice;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ApiResponse<T> {
	
	private LocalDateTime timeStamp;
	private T data;
	private ApiError error;
	
	public ApiResponse() {
		this.timeStamp = LocalDateTime.now();
	}
	
	public ApiResponse(T data) {
		this();
		this.data = data;
	}
	
	public ApiResponse(ApiError error) {
		this();
		this.error = error;
	}

}
