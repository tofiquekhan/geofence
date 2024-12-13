package com.inn.geofencemanage.impl;

import com.inn.geofencemanage.dto.GeofenceDTO;
import com.inn.geofencemanage.exception.ResourceNotFoundException;
import com.inn.geofencemanage.exception.ValidationException;
import com.inn.geofencemanage.geofencedir.entity.GeofenceEntity;
import com.inn.geofencemanage.repository.GeofenceRepository;
import com.inn.geofencemanage.service.GeofenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class GeofenceServiceImpl implements GeofenceService {

	@Autowired
	private GeofenceRepository geofenceRepository;

	@Override
	public GeofenceEntity createGeofence(GeofenceDTO geofencedto) {
		log.info("Creating a new geofence: {}", geofencedto.getGeofence_name());

		Optional.ofNullable(geofencedto.getGeofence_name()).filter(name -> !name.trim().isEmpty()).orElseThrow(() -> {
			log.warn("Geofence name is invalid.");
			return new ValidationException("Geofence name is required.");
		});

		Optional.ofNullable(geofencedto.getCoordinates()).filter(coords -> !coords.isEmpty()).orElseThrow(() -> {
			log.warn("Coordinates are missing for geofence {}", geofencedto.getGeofence_name());
			return new ValidationException("Geofence coordinates are required.");
		});

		List<double[]> coordinateData = new ArrayList<>();
		for (List<Double> coordinate : geofencedto.getCoordinates()) {
			if (coordinate.size() != 2) {
				log.error("Invalid coordinate size. Expected 2 elements per coordinate.");
				throw new ValidationException("Each coordinate must contain latitude and longitude.");
			}

			double[] coordArray = new double[coordinate.size()];
			for (int i = 0; i < coordinate.size(); i++) {
				coordArray[i] = coordinate.get(i);
			}
			coordinateData.add(coordArray);
		}

		List<Long> longList = new ArrayList<>();
		if (geofencedto.getAuthorizedVehicleIds() != null) {
			longList.addAll(geofencedto.getAuthorizedVehicleIds());
		}

		GeofenceEntity entity = new GeofenceEntity();
		entity.setGeofence_name(geofencedto.getGeofence_name());
		entity.setCoordinates(coordinateData); // Set the coordinates
		entity.setAuthorizedVehicles(longList); // Set authorized vehicle IDs

		GeofenceEntity createdGeofence = geofenceRepository.save(entity);
		log.info("Geofence created successfully with ID: {}", createdGeofence.getId());
		return createdGeofence;
	}

	@Override
	public List<GeofenceEntity> getAllGeofences() {
		log.info("Fetching all geofences");
		List<GeofenceEntity> geofences = geofenceRepository.findAll();

		if (geofences.isEmpty()) {
			log.error("No geofences found in the system.");
			throw new ResourceNotFoundException("No geofences found");
		}

		log.info("Successfully fetched {} geofences", geofences.size());
		return geofences;
	}

	@Override
	public GeofenceEntity getGeofenceById(Long id) {
		log.info("Fetching geofence with ID: {}", id);
		return geofenceRepository.findById(id).orElseThrow(() -> {
			log.error("Geofence with ID {} not found", id);
			return new ResourceNotFoundException("Geofence with ID " + id + " not found");
		});
	}

	@Override
	public void deleteGeofence(Long id) {
		log.info("Deleting geofence with ID: {}", id);
		GeofenceEntity geofence = geofenceRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Geofence with ID " + id + " not found"));

		geofenceRepository.delete(geofence);
		log.info("Geofence with ID {} deleted successfully", id);
	}

	@Override
	public GeofenceEntity updateGeofence(Long id, GeofenceDTO geofencedto) {
		log.info("Updating geofence with ID: {}", id);

		Optional.ofNullable(geofencedto.getGeofence_name()).filter(name -> !name.trim().isEmpty()).orElseThrow(() -> {
			log.error("Geofence name is invalid.");
			return new ValidationException("Geofence name is required.");
		});

		Optional.ofNullable(geofencedto.getCoordinates()).filter(coords -> !coords.isEmpty()).orElseThrow(() -> {
			log.error("Coordinates are missing for geofence {}", geofencedto.getGeofence_name());
			return new ValidationException("Geofence coordinates are required.");
		});

		GeofenceEntity geofenceEntity = geofenceRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Geofence not found with ID: " + id));

		geofenceEntity.setGeofence_name(geofencedto.getGeofence_name());

		List<double[]> coordinateData = new ArrayList<>();
		for (List<Double> coordinate : geofencedto.getCoordinates()) {
			if (coordinate.size() != 2) {
				log.error("Invalid coordinate size for geofence ID {}. Expected 2 elements per coordinate.", id);
				throw new ValidationException("Each coordinate must contain latitude and longitude.");
			}

			double[] coordArray = new double[coordinate.size()];
			for (int i = 0; i < coordinate.size(); i++) {
				coordArray[i] = coordinate.get(i);
			}
			coordinateData.add(coordArray);
		}

		List<Long> longList = new ArrayList<>();
		if (geofencedto.getAuthorizedVehicleIds() != null) {
			longList.addAll(geofencedto.getAuthorizedVehicleIds());
		}

		geofenceEntity.setAuthorizedVehicles(longList); // Set the list of authorized vehicles
		geofenceEntity.setCoordinates(coordinateData); // Set the coordinates

		GeofenceEntity updatedGeofence = geofenceRepository.save(geofenceEntity);
		log.info("Geofence with ID {} updated successfully", updatedGeofence.getId());
		return updatedGeofence; // Return the updated entity
	}
}
