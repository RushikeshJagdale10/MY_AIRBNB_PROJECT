package com.it.airbnb.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
		uniqueConstraints = @UniqueConstraint(
		name = "unique_hotel_room_date", 
		columnNames = {"hotel_id", "room_id", "date"}
		))
public class Inventory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="hotel_id", nullable = false) 
	private Hotel hotel;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="room_id", nullable = false)
	private Room room;
	
	@Column(nullable = false)
	private LocalDate date;
	
	@Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
	private Integer bookedCount;
	
	@Column(nullable = false)
	private Integer totalCount;
	
	@Column(nullable = false, precision = 5, scale = 2)
	private BigDecimal surgeFactor;
	
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal price;  //base price * surgeFactor
	
	@Column(nullable = false)
	private String city;
	
	@Column(nullable = false)
	private Boolean closed;
	
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	private LocalDateTime updatedAt;
}
