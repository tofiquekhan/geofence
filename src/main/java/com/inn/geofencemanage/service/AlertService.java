package com.inn.geofencemanage.service;

import java.util.List;

import com.inn.geofencemanage.geofencedir.entity.AlertEntity;

public interface AlertService {

	List<AlertEntity> getAllAlerts(); // Method to fetch all alerts

}
