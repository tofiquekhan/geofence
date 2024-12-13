package com.inn.geofencemanage.geofencedir.entity;

import java.time.LocalDateTime;
import com.inn.geofencemanage.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Alert")
public class AlertEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "vehicle_id")
	private VehicleEntity vehicle;

	@ManyToOne
	@JoinColumn(name = "geofence_id")
	private GeofenceEntity geofence;

	@Column(name = "alert_type", nullable = false)
	private String alertType;

	@Column(name = "timestamp", nullable = false)
	private LocalDateTime timestamp;

}
