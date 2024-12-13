package com.inn.geofencemanage.service;

import java.util.List;

import com.inn.geofencemanage.dto.GeofenceDTO;
import com.inn.geofencemanage.geofencedir.entity.GeofenceEntity;

public interface GeofenceService {

	GeofenceEntity createGeofence(GeofenceDTO geofence);

	List<GeofenceEntity> getAllGeofences();

	GeofenceEntity getGeofenceById(Long id);

	GeofenceEntity updateGeofence(Long id, GeofenceDTO geofenceDetails);

	void deleteGeofence(Long id);
}
