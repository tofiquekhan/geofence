package com.inn.geofencemanage.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.inn.geofencemanage.dto.GeofenceDTO;
import com.inn.geofencemanage.exception.ResourceNotFoundException;
import com.inn.geofencemanage.exception.ValidationException;
import com.inn.geofencemanage.geofencedir.entity.GeofenceEntity;
import com.inn.geofencemanage.service.GeofenceService;
import java.util.List;

@RestController
@RequestMapping("/api/geofences")
public class GeofenceController {

	private static final Logger logger = LoggerFactory.getLogger(GeofenceController.class);

	@Autowired
	private GeofenceService geofenceService;

	@PostMapping("/create")
	public GeofenceEntity createGeofence(@RequestBody GeofenceDTO geofence) {
		logger.info("Request received to create a new Geofence: {}", geofence.getGeofence_name());
		GeofenceEntity createdGeofence = geofenceService.createGeofence(geofence);
		logger.info("Geofence created successfully with ID: {}", createdGeofence.getId());
		return createdGeofence;
	}

	@GetMapping("/getAll")
	public List<GeofenceEntity> getAllGeofences() {
		logger.info("Request received to fetch all geofences");
		List<GeofenceEntity> geofences = geofenceService.getAllGeofences();
		logger.info("Successfully fetched {} geofences", geofences.size());
		return geofences;
	}

	@GetMapping("/get/{id}")
	public GeofenceEntity getGeofenceById(@PathVariable Long id) {
		logger.info("Request received to fetch geofence with ID: {}", id);
		GeofenceEntity geofence = geofenceService.getGeofenceById(id);
		logger.info("Geofence fetched successfully: {}", geofence);
		return geofence;
	}

	@PutMapping("/update/{id}")
	public GeofenceEntity updateGeofence(@PathVariable Long id, @RequestBody GeofenceDTO geofenceDetails) {
		logger.info("Request received to update geofence with ID: {}", id);
		GeofenceEntity updatedGeofence = geofenceService.updateGeofence(id, geofenceDetails);

		if (updatedGeofence != null) {
			logger.info("Geofence with ID {} updated successfully", updatedGeofence.getId());
			return updatedGeofence;
		} else {
			throw new ResourceNotFoundException("Geofence with ID " + id + " not found for update");
		}
	}

	@DeleteMapping("/delete/{id}")
	public void deleteGeofence(@PathVariable Long id) {
		logger.info("Request received to delete geofence with ID: {}", id);
		geofenceService.deleteGeofence(id);
		logger.info("Geofence with ID {} deleted successfully", id);
	}

}
