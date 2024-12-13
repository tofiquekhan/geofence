package com.inn.geofencemanage.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inn.geofencemanage.dto.VehicleDTO;
import com.inn.geofencemanage.exception.ResourceNotFoundException;
import com.inn.geofencemanage.geofencedir.entity.AlertEntity;
import com.inn.geofencemanage.geofencedir.entity.AuthorizationStatus;
import com.inn.geofencemanage.geofencedir.entity.GeofenceEntity;
import com.inn.geofencemanage.geofencedir.entity.VehicleEntity;
import com.inn.geofencemanage.repository.AlertRepository;
import com.inn.geofencemanage.repository.GeofenceRepository;
import com.inn.geofencemanage.repository.VehicleRepository;
import com.inn.geofencemanage.service.VehicleService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {

	private static final Logger logger = LoggerFactory.getLogger(VehicleServiceImpl.class);

	@Autowired
	private VehicleRepository vehicleRepository;

	@Autowired
	private GeofenceRepository geofenceRepository;

	@Autowired
	private AlertRepository alertRepository;

	@Transactional
	@Override
	public VehicleEntity createVehicle(VehicleDTO vehicleDTO) {
		logger.info("Creating new vehicle with vehicle number: {}", vehicleDTO.getVehicleNumber());

		VehicleEntity vehicleEntity = new VehicleEntity();
		vehicleEntity.setVehicleName(vehicleDTO.getVehicleName());
		vehicleEntity.setVehicleNumber(vehicleDTO.getVehicleNumber());
		vehicleEntity.setEntryTime(LocalDateTime.now());

		vehicleEntity.setLatitude(vehicleDTO.getLatitude());
		vehicleEntity.setLongitude(vehicleDTO.getLongitude());

		GeofenceEntity authorizedGeofence = checkGeofenceAuthorization(vehicleDTO, true); // True for create action

		if (authorizedGeofence != null && authorizedGeofence.getId() == null) {
			geofenceRepository.save(authorizedGeofence);
		}

		if (authorizedGeofence == null) {
			logger.warn("Vehicle {} is not authorized for any geofence. Creating alert.",
					vehicleEntity.getVehicleNumber());
			vehicleEntity.setAuthorizationStatus(AuthorizationStatus.UNAUTHORIZED); // Set status as UNAUTHORIZED
			createAlert(vehicleEntity, "Unauthorized Access");
		} else {
			logger.info("Vehicle {} is authorized in geofence {}", vehicleEntity.getVehicleNumber(),
					authorizedGeofence.getGeofence_name());
			vehicleEntity.setAuthorizationStatus(AuthorizationStatus.AUTHORIZED); // Set status as AUTHORIZED
			createAlert(vehicleEntity, "Authorized");
		}

		vehicleEntity.setGeofence(authorizedGeofence);

		return vehicleRepository.save(vehicleEntity);
	}

	private GeofenceEntity checkGeofenceAuthorization(VehicleDTO vehicle, Boolean isCreate) {
		List<GeofenceEntity> geofences = geofenceRepository.findAll();

		for (GeofenceEntity geofence : geofences) {
			List<Double[]> coordinates = convertStringToCoordinates(geofence.getCoordinates());
			if (isPointInPolygon(vehicle.getLatitude(), vehicle.getLongitude(), coordinates)) {
				if (isCreate) {
					List<Long> authorizedVehicles = geofence.getAuthorizedVehicles();
					if (!authorizedVehicles.contains(vehicle.getId())) {
						authorizedVehicles.add(vehicle.getId()); // Add vehicle ID to the authorized list
						geofence.setAuthorizedVehicles(authorizedVehicles);
						geofenceRepository.save(geofence); // Save the updated geofence
					}
				} else {
					List<Long> authorizedVehicles = geofence.getAuthorizedVehicles();
					if (authorizedVehicles.contains(vehicle.getId())) {
						return geofence; // Authorized vehicle found
					} else {
						authorizedVehicles.remove(vehicle.getId());
						geofence.setAuthorizedVehicles(authorizedVehicles);
						geofenceRepository.save(geofence); // Save the updated geofence
						VehicleEntity vehicleEntity = new VehicleEntity();

						createAlert(vehicleEntity, "Unauthorized Access"); // Create alert for unauthorized access
					}
				}
				return geofence;
			}
		}

		return null;
	}

	private List<Double[]> convertStringToCoordinates(List<double[]> list) {
		List<Double[]> coordinates = new ArrayList<>();
		for (double[] coordinate : list) {
			Double[] coordinatePair = new Double[2];
			coordinatePair[0] = coordinate[0]; // Latitude
			coordinatePair[1] = coordinate[1]; // Longitude
			coordinates.add(coordinatePair);
		}
		return coordinates;
	}

	private void createAlert(VehicleEntity vehicle, String alertType) {
		AlertEntity alert = new AlertEntity();
		alert.setVehicle(vehicle); // Set the vehicle in the alert
		alert.setAlertType(alertType); // Set the alert type (e.g., "Unauthorized Access")
		alert.setTimestamp(LocalDateTime.now()); // Set the current timestamp

		alertRepository.save(alert); // Save the alert in the database
	}

	private boolean isPointInPolygon(double latitude, double longitude, List<Double[]> coordinates) {
		int n = coordinates.size();
		boolean inside = false;

		for (int i = 0, j = n - 1; i < n; j = i++) {
			Double[] p1 = coordinates.get(i);
			Double[] p2 = coordinates.get(j);

			if (longitude > Math.min(p1[1], p2[1])) {
				if (longitude <= Math.max(p1[1], p2[1])) {
					if (latitude <= Math.max(p1[0], p2[0])) {
						if (p1[1] != p2[1]) {
							double xinters = (longitude - p1[1]) * (p2[0] - p1[0]) / (p2[1] - p1[1]) + p1[0];
							if (p1[1] == p2[1] || latitude <= xinters) {
								inside = !inside;
							}
						}
					}
				}
			}
		}
		return inside;
	}

	@Override
	public VehicleEntity getVehicleById(Long id) {
		logger.info("Fetching vehicle with ID: {}", id);
		return vehicleRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + id));
	}

	@Override
	@Transactional
	public VehicleEntity updateVehicle(Long id, VehicleDTO vehicleDTO) {
		logger.info("Updating vehicle with ID: {}", id);
		

		VehicleEntity vehicleEntity = vehicleRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + id));

		LocalDateTime oldEntryTime = vehicleEntity.getEntryTime();
		LocalDateTime newEntryTime = vehicleDTO.getEntryTime();
	

		vehicleEntity.setVehicleName(vehicleDTO.getVehicleName());
		vehicleEntity.setVehicleNumber(vehicleDTO.getVehicleNumber());

		if (newEntryTime != null) {
			vehicleEntity.setEntryTime(newEntryTime); // Update entry time if provided
		}

		GeofenceEntity authorizedGeofence = null;
		if (vehicleDTO.getLatitude() != null && vehicleDTO.getLongitude() != null) {
			authorizedGeofence = checkGeofenceAuthorization(vehicleDTO, false); // False for update action
		}

		if (authorizedGeofence != null) {
			// Vehicle is authorized
			vehicleEntity.setGeofence(authorizedGeofence);
			vehicleEntity.setAuthorizationStatus(AuthorizationStatus.AUTHORIZED); // Authorized status
		} else {
			if (oldEntryTime != null) {
				Long stayDuration = java.time.Duration.between(oldEntryTime, LocalDateTime.now()).toMinutes();
				vehicleEntity.setStayDuration(stayDuration); // Set the stay duration in minutes
			}

			vehicleEntity.setExitTime(LocalDateTime.now());
			vehicleEntity.setAuthorizationStatus(AuthorizationStatus.UNAUTHORIZED); // Unauthorized status

			if (vehicleEntity.getGeofence() != null) {
				GeofenceEntity oldGeofence = vehicleEntity.getGeofence();
				List<Long> authorizedVehicles = oldGeofence.getAuthorizedVehicles();
				authorizedVehicles.remove(vehicleEntity.getId());
				oldGeofence.setAuthorizedVehicles(authorizedVehicles);
				geofenceRepository.save(oldGeofence); // Save updated geofence
			}
		}

		return vehicleRepository.save(vehicleEntity);
	}

	@Override
	@Transactional
	public void deleteVehicle(Long id) {
		logger.info("Deleting vehicle with ID: {}", id);

		VehicleEntity vehicleEntity = vehicleRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + id));

		alertRepository.deleteByVehicleId(id);

		vehicleRepository.delete(vehicleEntity); // Now safe to delete the vehicle
	}

	@Override
	public List<VehicleEntity> getAllVehicles() {
		logger.info("Fetching all vehicles from the database");
		return vehicleRepository.findAll();
	}
}
