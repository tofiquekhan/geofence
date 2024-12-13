package com.inn.geofencemanage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.inn.geofencemanage.dto.VehicleDTO;
import com.inn.geofencemanage.exception.ResourceNotFoundException;
import com.inn.geofencemanage.geofencedir.entity.VehicleEntity;
import com.inn.geofencemanage.response.ApiResponse;
import com.inn.geofencemanage.service.VehicleService;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

	@Autowired
	private VehicleService vehicleService;

	@PostMapping("/create")
	public ResponseEntity<ApiResponse> createVehicle(@RequestBody VehicleDTO vehicleDTO) {
		VehicleEntity createdVehicle = vehicleService.createVehicle(vehicleDTO);

		ApiResponse response = new ApiResponse(true, "Vehicle created successfully", createdVehicle);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<VehicleEntity>> getAllVehicles() {
		List<VehicleEntity> vehicles = vehicleService.getAllVehicles();
		return new ResponseEntity<>(vehicles, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<VehicleEntity> getVehicleById(@PathVariable Long id) {
		VehicleEntity vehicle = vehicleService.getVehicleById(id);
		return new ResponseEntity<>(vehicle, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<VehicleEntity> updateVehicle(@PathVariable Long id, @RequestBody VehicleDTO vehicleDTO) {
		VehicleEntity updatedVehicle = vehicleService.updateVehicle(id, vehicleDTO);
		return new ResponseEntity<>(updatedVehicle, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteVehicle(@PathVariable Long id) {
		try {
			vehicleService.deleteVehicle(id);

			ApiResponse response = new ApiResponse(true, "Vehicle deleted successfully", null);
			return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
		} catch (ResourceNotFoundException e) {
			ApiResponse response = new ApiResponse(false, "Vehicle not found with ID: " + id, null);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			ApiResponse response = new ApiResponse(false, "An unexpected error occurred: " + e.getMessage(), null);
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
