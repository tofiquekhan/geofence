package com.inn.geofencemanage.geofencedir.entity;

import com.inn.geofencemanage.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Transactional
public class VehicleEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "vehicle_name", nullable = false)
	private String vehicleName;

	@Column(name = "vehicle_number", nullable = false, unique = true)
	private String vehicleNumber;

	@Column(name = "latitude")
	private Double latitude;

	@Column(name = "longitude")
	private Double longitude;

	@Column(name = "entry_time")
	private LocalDateTime entryTime;

	@Column(name = "exit_time")
	private LocalDateTime exitTime;

	@Column(name = "stay_duration")
	private Long stayDuration;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "geofence_id")
	private GeofenceEntity geofence;

	@Enumerated(EnumType.STRING)
	@Column(name = "authorization_status")
	private AuthorizationStatus authorizationStatus;

}
