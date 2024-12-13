package com.inn.geofencemanage.impl;

import com.inn.geofencemanage.dto.HistoricalReportDTO;
import com.inn.geofencemanage.geofencedir.entity.AlertEntity;
import com.inn.geofencemanage.geofencedir.entity.VehicleEntity;
import com.inn.geofencemanage.repository.AlertRepository;
import com.inn.geofencemanage.repository.VehicleRepository;
import com.inn.geofencemanage.service.HistoricalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HistoricalServiceImpl implements HistoricalService {

	@Autowired
	private VehicleRepository vehicleRepository;

	@Autowired
	private AlertRepository alertRepository;

	@Override
	public List<HistoricalReportDTO> generateHistoricalReport() {
		List<VehicleEntity> vehicles = vehicleRepository.findAll();
		List<HistoricalReportDTO> reportData = new ArrayList<>();

		for (VehicleEntity vehicle : vehicles) {
			try {
				HistoricalReportDTO reportDTO = new HistoricalReportDTO();
				reportDTO.setVehicleId(vehicle.getId());

				// Check if geofence exists for the vehicle
				if (vehicle.getGeofence() != null) {
					reportDTO.setGeofenceName(vehicle.getGeofence().getGeofence_name());
				} else {
					reportDTO.setGeofenceName("No Geofence Assigned");
				}

				reportDTO.setEntryTimestamp(vehicle.getEntryTime());
				reportDTO.setExitTimestamp(vehicle.getExitTime());
				reportDTO.setDuration(vehicle.getStayDuration());
				reportDTO.setAuthorizationStatus(vehicle.getAuthorizationStatus().toString());

				// Fetch associated alerts
				List<AlertEntity> alerts = alertRepository.findByVehicle(vehicle);
				List<String> alertMessages = new ArrayList<>();
				for (AlertEntity alert : alerts) {
					alertMessages.add(alert.getAlertType());
				}
				reportDTO.setAlerts(alertMessages);

				reportData.add(reportDTO);
			} catch (Exception e) {
				System.err.println("Error processing vehicle with ID: " + vehicle.getId());
				e.printStackTrace(); // Add detailed logging of the error
			}
		}

		return reportData;
	}

}
