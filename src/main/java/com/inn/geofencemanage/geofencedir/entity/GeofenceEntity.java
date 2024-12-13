package com.inn.geofencemanage.geofencedir.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.Polygon;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inn.geofencemanage.base.BaseEntity;
import com.inn.geofencemanage.converter.JsonAttributeConverter;
import com.inn.geofencemanage.converter.LongListAttributeConverter;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "geofence_managements")
public class GeofenceEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "geofence_name", nullable = false)
	private String geofence_name;

	@Convert(converter = JsonAttributeConverter.class)
	@Lob
	@Column(name = "coordinates", columnDefinition = "JSON")
	private List<double[]> coordinates;

	@Convert(converter = LongListAttributeConverter.class)
	@Column(name = "authorized_vehicles", columnDefinition = "JSON")
	private List<Long> authorizedVehicles;

}
