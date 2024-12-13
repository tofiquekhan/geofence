package com.inn.geofencemanage.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.inn.geofencemanage.geofencedir.entity.VehicleEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<VehicleEntity, Long> {
	  Optional<VehicleEntity> findByVehicleNumber(String vehicleNumber);
	  //  List<VehicleEntity> findByEntryTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

	    List<VehicleEntity> findAll(); // No filtering, fetch all vehicles

}


