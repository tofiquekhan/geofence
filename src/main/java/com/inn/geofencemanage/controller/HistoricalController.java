package com.inn.geofencemanage.controller;

import com.inn.geofencemanage.dto.HistoricalReportDTO;
import com.inn.geofencemanage.exception.ResourceNotFoundException;
import com.inn.geofencemanage.response.ApiResponse;
import com.inn.geofencemanage.response.ResponseHandler;
import com.inn.geofencemanage.service.HistoricalService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/historical")  
@Slf4j
public class HistoricalController {

    @Autowired
    private HistoricalService historicalService;

 
    @GetMapping("/report")
    public ResponseEntity<ApiResponse> getHistoricalReport() {
		log.info("fetch all Historical Report");

        try {
            List<HistoricalReportDTO> report = historicalService.generateHistoricalReport();

			return ResponseHandler.generateResponse("Historical report generated successfully", HttpStatus.OK, report);

        } catch (ResourceNotFoundException e) {
            
			return ResponseHandler.generateResponse("An unexpected error occurred while generating the historical report", HttpStatus.INTERNAL_SERVER_ERROR, null);

        }
    }
}
