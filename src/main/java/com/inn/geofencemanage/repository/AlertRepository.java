package com.inn.geofencemanage.repository;

import com.inn.geofencemanage.geofencedir.entity.AlertEntity;
import com.inn.geofencemanage.geofencedir.entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<AlertEntity, Long> {

	List<AlertEntity> findByVehicle(VehicleEntity vehicle);

	void deleteByVehicleId(Long vehicleId);

	List<AlertEntity> findAll();

	List<AlertEntity> findByGeofenceId(Long id);
}
