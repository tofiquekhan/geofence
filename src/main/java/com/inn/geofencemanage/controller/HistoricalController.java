package com.inn.geofencemanage.controller;

import com.inn.geofencemanage.dto.HistoricalReportDTO;
import com.inn.geofencemanage.exception.ResourceNotFoundException;
import com.inn.geofencemanage.response.ApiResponse;
import com.inn.geofencemanage.service.HistoricalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/historical")  
public class HistoricalController {

    @Autowired
    private HistoricalService historicalService;

 
    @GetMapping("/report")
    public ResponseEntity<ApiResponse> getHistoricalReport() {
        try {
            List<HistoricalReportDTO> report = historicalService.generateHistoricalReport();

            ApiResponse response = new ApiResponse(true, "Historical report generated successfully", report);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            ApiResponse response = new ApiResponse(false, "An unexpected error occurred while generating the historical report", null);
            return ResponseEntity.status(500).body(response);
        }
    }
}
