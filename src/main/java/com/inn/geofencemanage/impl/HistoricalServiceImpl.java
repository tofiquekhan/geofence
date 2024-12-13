package com.inn.geofencemanage.impl;

import com.inn.geofencemanage.dto.HistoricalReportDTO;
import com.inn.geofencemanage.geofencedir.entity.AlertEntity;
import com.inn.geofencemanage.geofencedir.entity.VehicleEntity;
import com.inn.geofencemanage.repository.AlertRepository;
import com.inn.geofencemanage.repository.VehicleRepository;
import com.inn.geofencemanage.service.HistoricalService;

import com.inn.geofencemanage.exception.ResourceNotFoundException;
import com.inn.geofencemanage.exception.ValidationException; // Import ValidationException
import lombok.extern.slf4j.Slf4j; // Import Lombok's @Slf4j

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class HistoricalServiceImpl implements HistoricalService {

	@Autowired
	private VehicleRepository vehicleRepository;

	@Autowired
	private AlertRepository alertRepository;

	@Override
	public List<HistoricalReportDTO> generateHistoricalReport() {
		List<VehicleEntity> vehicles = vehicleRepository.findAll();

		if (vehicles.isEmpty()) {
			log.error("No vehicles found in the system.");
			throw new ResourceNotFoundException("No vehicles found");
		}

		List<HistoricalReportDTO> reportData = new ArrayList<>();

		for (VehicleEntity vehicle : vehicles) {
			// Check if vehicle data is valid before processing
			Optional.ofNullable(vehicle).filter(v -> v.getId() != null && v.getVehicleName() != null
					&& v.getLatitude() != null && v.getLongitude() != null).orElseThrow(() -> {
						log.warn("Invalid vehicle data for vehicle with ID: {}",
								vehicle != null ? vehicle.getId() : "null");
						return new ValidationException(
								"Vehicle data is invalid for ID: " + (vehicle != null ? vehicle.getId() : "null"));
					});

			HistoricalReportDTO reportDTO = new HistoricalReportDTO();
			reportDTO.setVehicleId(vehicle.getId());

			log.info("Processing vehicle with ID: {}", vehicle.getId());

			if (vehicle.getGeofence() != null) {
				reportDTO.setGeofenceName(vehicle.getGeofence().getGeofence_name());
			} else {
				reportDTO.setGeofenceName("No Geofence Assigned");
				log.warn("No Geofence assigned to vehicle with ID: {}", vehicle.getId());
			}

			reportDTO.setEntryTimestamp(vehicle.getEntryTime());
			reportDTO.setExitTimestamp(vehicle.getExitTime());
			reportDTO.setDuration(vehicle.getStayDuration());

			if (vehicle.getAuthorizationStatus() != null) {
				reportDTO.setAuthorizationStatus(vehicle.getAuthorizationStatus().toString());
			} else {
				reportDTO.setAuthorizationStatus("Unknown");
				log.warn("Authorization status is null for vehicle ID: {}", vehicle.getId());
			}

			// Handle alerts
			List<AlertEntity> alerts = alertRepository.findByVehicle(vehicle);
			List<String> alertMessages = new ArrayList<>();
			for (AlertEntity alert : alerts) {
				alertMessages.add(alert.getAlertType());
			}
			reportDTO.setAlerts(alertMessages);

			reportData.add(reportDTO);
		}

		log.info("Historical report generation completed with {} vehicles processed.", reportData.size());
		return reportData;
	}
}
