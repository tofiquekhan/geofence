package com.inn.geofencemanage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeofenceDTO {

	private String geofence_name;
	private List<List<Double>> coordinates;
	private List<Long> authorizedVehicleIds = new ArrayList<>(); // Default to empty list

}
