package com.inn.geofencemanage.service;

import java.util.List;

import com.inn.geofencemanage.dto.HistoricalReportDTO;

public interface HistoricalService {

	List<HistoricalReportDTO> generateHistoricalReport();

}
