package com.inn.geofencemanage.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class VehicleDTO {

	private Long id;

	private String vehicleName;

	private String vehicleNumber;

	private Double latitude;

	private Double longitude;

	private LocalDateTime timestamp;

	private LocalDateTime entryTime;

	private LocalDateTime exitTime;

	private Long stayDuration;

}
