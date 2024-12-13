package com.inn.geofencemanage.service;

import java.util.List;

import com.inn.geofencemanage.dto.VehicleDTO;
import com.inn.geofencemanage.geofencedir.entity.VehicleEntity;

public interface VehicleService {

	VehicleEntity createVehicle(VehicleDTO vehicleDTO);

	List<VehicleEntity> getAllVehicles();

	VehicleEntity getVehicleById(Long id);

	VehicleEntity updateVehicle(Long id, VehicleDTO vehicleDTO);

	void deleteVehicle(Long id);
}
