package com.inn.geofencemanage.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.inn.geofencemanage.dto.GeofenceDTO;
import com.inn.geofencemanage.geofencedir.entity.GeofenceEntity;
import com.inn.geofencemanage.response.ApiResponse;
import com.inn.geofencemanage.response.ResponseHandler;
import com.inn.geofencemanage.service.GeofenceService;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequestMapping("/api/geofences")
@Slf4j
public class GeofenceController {

	@Autowired
	private GeofenceService geofenceService;

	@PostMapping("/create")
	public ResponseEntity<ApiResponse> createGeofence(@RequestBody GeofenceDTO geofence) {
		log.info("createGeofence Started: {}", geofence.getGeofence_name());

		GeofenceEntity createdGeofence = geofenceService.createGeofence(geofence);
		log.info("Geofence created successfully with ID: {}", createdGeofence.getId());

		return ResponseHandler.generateResponse("Geofence created successfully", HttpStatus.CREATED, createdGeofence);

	}

	@GetMapping("/getAll")
	public ResponseEntity<ApiResponse> getAllGeofences() {
		log.info("fetch all geofences");

		List<GeofenceEntity> geofences = geofenceService.getAllGeofences();
		log.info("Fetched {} geofences successfully", geofences.size());

		return ResponseHandler.generateResponse("Fetched geofences successfully", HttpStatus.OK, geofences);

	}

	@GetMapping("/get/{id}")
	public ResponseEntity<ApiResponse> getGeofenceById(@PathVariable Long id) {
		log.info("get Geofence with ID: {}", id);

		GeofenceEntity geofence = geofenceService.getGeofenceById(id);
		if (geofence != null) {
			log.info("Geofence with ID {} fetched successfully", id);
			return ResponseHandler.generateResponse("Geofence fetched successfully", HttpStatus.OK, geofence);

		} else {
			log.warn("Geofence with ID {} not found", id);

			return ResponseHandler.generateResponse("Geofence with ID " + id + " not found", HttpStatus.NOT_FOUND,
					null);

		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<ApiResponse> updateGeofence(@PathVariable Long id, @RequestBody GeofenceDTO geofenceDetails) {
		log.info("update Geofence By ID: {}", id);

		GeofenceEntity updatedGeofence = geofenceService.updateGeofence(id, geofenceDetails);

		if (updatedGeofence != null) {
			log.info("Geofence with ID {} updated successfully", updatedGeofence.getId());
			return ResponseHandler.generateResponse("Geofence updated successfully", HttpStatus.OK, updatedGeofence);

		} else {
			log.warn("Geofence with ID {} not found for update", id);

			return ResponseHandler.generateResponse("Geofence with ID " + id + " not found for update",
					HttpStatus.NOT_FOUND, null);

		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ApiResponse> deleteGeofence(@PathVariable Long id) {
		log.info("delete Geofence By ID: {}", id);

		geofenceService.deleteGeofence(id);
		log.info("Geofence with ID {} deleted successfully", id);

		return ResponseHandler.generateResponse("Geofence with ID " + id + " deleted successfully",
				HttpStatus.NO_CONTENT, null);

	}
}
