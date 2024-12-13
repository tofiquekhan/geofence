package com.inn.geofencemanage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.geofencemanage.exception.ResourceNotFoundException;
import com.inn.geofencemanage.geofencedir.entity.AlertEntity;
import com.inn.geofencemanage.repository.AlertRepository;
import com.inn.geofencemanage.service.AlertService;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Service
public class AlertServiceImpl implements AlertService {

	@Autowired
	private AlertRepository alertRepository;

	@Override
	public List<AlertEntity> getAllAlerts() {
		log.info("Fetching all alerts from the repository");

		List<AlertEntity> alerts = alertRepository.findAll();

		
		if (alerts.isEmpty()) {
			log.error("No alerts found");
			throw new ResourceNotFoundException("No alerts found");
		}

		return alerts; 
	}
}
