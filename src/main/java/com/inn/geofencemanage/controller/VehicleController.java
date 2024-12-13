package com.inn.geofencemanage.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.inn.geofencemanage.dto.VehicleDTO;
import com.inn.geofencemanage.exception.ResourceNotFoundException;
import com.inn.geofencemanage.geofencedir.entity.VehicleEntity;
import com.inn.geofencemanage.response.ApiResponse;
import com.inn.geofencemanage.response.ResponseHandler;
import com.inn.geofencemanage.service.VehicleService;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@Slf4j
public class VehicleController {

	@Autowired
	private VehicleService vehicleService;

	@PostMapping("/create")
	public ResponseEntity<ApiResponse> createVehicle(@RequestBody VehicleDTO vehicleDTO) {
		log.info("createvehicle Started: {}", vehicleDTO);

		VehicleEntity createdVehicle = vehicleService.createVehicle(vehicleDTO);

		log.info("Vehicle created successfully with ID: {}", createdVehicle.getId());

	
		return ResponseHandler.generateResponse("Vehicle created successfully", HttpStatus.CREATED, createdVehicle);

	}

	@GetMapping
	public ResponseEntity<List<VehicleEntity>> getAllVehicles() {
		log.info("fetch all vehicles");

		List<VehicleEntity> vehicles = vehicleService.getAllVehicles();

		log.info("Fetched {} vehicles successfully", vehicles.size());

		return new ResponseEntity<>(vehicles, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<VehicleEntity> getVehicleById(@PathVariable Long id) {
		log.info("get vehicle By ID: {}", id);

		VehicleEntity vehicle = vehicleService.getVehicleById(id);
		if (vehicle != null) {
			log.info("Vehicle with ID {} fetched successfully", id);
		} else {
			log.warn("Vehicle with ID {} not found", id);
			throw new ResourceNotFoundException("Vehicle not found with ID: " + id);
		}

		return new ResponseEntity<>(vehicle, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<VehicleEntity> updateVehicle(@PathVariable Long id, @RequestBody VehicleDTO vehicleDTO) {
		log.info("update vehicle By ID: {}", id);

		VehicleEntity updatedVehicle = vehicleService.updateVehicle(id, vehicleDTO);

		if (updatedVehicle != null) {
			log.info("Vehicle with ID {} updated successfully", updatedVehicle.getId());
		} else {
			log.warn("Vehicle with ID {} not found for update", id);
			throw new ResourceNotFoundException("Vehicle not found with ID: " + id);
		}

		return new ResponseEntity<>(updatedVehicle, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteVehicle(@PathVariable Long id) {
		log.info("delete vehicle By ID: {}", id);

		try {
			vehicleService.deleteVehicle(id);

			log.info("Vehicle with ID {} deleted successfully", id);

			
			return ResponseHandler.generateResponse("Vehicle deleted successfully", HttpStatus.NO_CONTENT, null);

		} catch (ResourceNotFoundException e) {
			log.error("Error occurred while deleting vehicle with ID {}: {}", id, e.getMessage());
			
			return ResponseHandler.generateResponse("Vehicle not found with ID: ", HttpStatus.NOT_FOUND, null);

		} catch (Exception e) {
			log.error("Unexpected error occurred while deleting vehicle with ID {}: {}", id, e.getMessage());
			
			return ResponseHandler.generateResponse("An unexpected error occurred: ", HttpStatus.INTERNAL_SERVER_ERROR, null);

		}
	}
}
