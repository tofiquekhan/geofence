package com.inn.geofencemanage.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class HistoricalReportDTO {
	private Long vehicleId;
	private String geofenceName;
	private LocalDateTime entryTimestamp;
	private LocalDateTime exitTimestamp;
	private Long duration;
	private String authorizationStatus;
	private List<String> alerts;
}
