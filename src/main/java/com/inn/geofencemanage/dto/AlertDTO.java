package com.inn.geofencemanage.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AlertDTO {

	private Long id;

	private Long vehicleId;

	private Long geofenceId;

	private String alertType;

	private LocalDateTime timestamp;

	private String message;
}
