package com.inn.geofencemanage.dto;

import jakarta.persistence.*;
import lombok.Data;

@Data
public class HostoricalDTO {

	private String entryTimestamp;

	private String exitTimestamp;

	private long durationInMinutes;

	private String authorizationStatus;

	private String alertMessage;

}
