package com.inn.geofencemanage.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.geofencemanage.geofencedir.entity.AlertEntity;
import com.inn.geofencemanage.repository.AlertRepository;
import com.inn.geofencemanage.service.AlertService;

import java.util.List;  // Import List

@Service
public class AlertServiceImpl implements AlertService {

    private static final Logger logger = LoggerFactory.getLogger(AlertServiceImpl.class);

    @Autowired
    private AlertRepository alertRepository;

    @Override
    public List<AlertEntity> getAllAlerts() {
        logger.info("Fetching all alerts from the repository");
        try {
            return alertRepository.findAll();  // Fetch all alerts from the database
        } catch (Exception ex) {
            logger.error("Error occurred while fetching all alerts: {}", ex.getMessage());
            throw new RuntimeException("Unexpected error occurred while fetching alerts", ex);
        }
    }
}
