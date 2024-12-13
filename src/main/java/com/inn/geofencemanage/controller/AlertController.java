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
import com.inn.geofencemanage.response.ResponseHandler;
import com.inn.geofencemanage.service.AlertService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/alerts")
public class AlertController {

	@Autowired
	private AlertService alertService;

	@GetMapping("/all")
	public ResponseEntity<ApiResponse> getAllAlerts() {
		log.info("fetch all Alert");

		List<AlertEntity> alerts = alertService.getAllAlerts();
		log.info("Fetched {} Alert successfully", alerts.size());
		return ResponseHandler.generateResponse("Alerts fetched successfully", HttpStatus.OK, alerts);

	}
}
