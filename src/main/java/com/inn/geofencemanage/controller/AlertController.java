package com.inn.geofencemanage.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.inn.geofencemanage.geofencedir.entity.AlertEntity;
import com.inn.geofencemanage.response.ApiResponse;
import com.inn.geofencemanage.service.AlertService;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

	private static final Logger logger = LoggerFactory.getLogger(AlertController.class);

	@Autowired
	private AlertService alertService;

	@GetMapping("/all")
	public ResponseEntity<ApiResponse> getAllAlerts() {
		logger.info("Request received to fetch all alerts");
		try {

			List<AlertEntity> alerts = alertService.getAllAlerts();
			return ResponseEntity.ok(new ApiResponse(true, "Alerts fetched successfully", alerts));
		} catch (Exception e) {
			logger.error("Error occurred while fetching alerts: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse(false, "Error occurred while fetching alerts", null));
		}
	}
}
