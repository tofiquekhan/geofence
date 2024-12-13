package com.inn.geofencemanage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inn.geofencemanage.dto.GeofenceDTO;
import com.inn.geofencemanage.geofencedir.entity.GeofenceEntity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

@Repository
public interface GeofenceRepository extends JpaRepository<GeofenceEntity, Long> {
	@Query("SELECT g FROM GeofenceEntity g WHERE g.geofence_name = :name")
	Optional<GeofenceDTO> findByName(@Param("name") String name);

}
