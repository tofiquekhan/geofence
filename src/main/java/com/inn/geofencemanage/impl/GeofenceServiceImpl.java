package com.inn.geofencemanage.impl;


import org.locationtech.jts.geom.LinearRing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inn.geofencemanage.dto.GeofenceDTO;
import com.inn.geofencemanage.exception.ResourceNotFoundException;
import com.inn.geofencemanage.geofencedir.entity.GeofenceEntity;
import com.inn.geofencemanage.repository.GeofenceRepository;
import com.inn.geofencemanage.service.GeofenceService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.WKTReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class GeofenceServiceImpl implements GeofenceService {

    private static final Logger logger = LoggerFactory.getLogger(GeofenceServiceImpl.class);

    @Autowired
    private GeofenceRepository geofenceRepository;

 

    @Override
    public GeofenceEntity createGeofence(GeofenceDTO geofencedto) {
        try {
            logger.info("Creating a new geofence: {}", geofencedto.getGeofence_name());
            GeofenceEntity entity = new GeofenceEntity();

            List<double[]> coordinateData = new ArrayList<>();
            for (List<Double> coordinate : geofencedto.getCoordinates()) {
                double[] coordArray = new double[coordinate.size()];
                for (int i = 0; i < coordinate.size(); i++) {
                    coordArray[i] = coordinate.get(i);
                }
                coordinateData.add(coordArray);
            }

            List<Long> longList = new ArrayList<>();
            if (geofencedto.getAuthorizedVehicleIds() != null) {
                for (Long id : geofencedto.getAuthorizedVehicleIds()) {
                    longList.add(id);  // No need to convert Long to String
                }
            }

            entity.setAuthorizedVehicles(longList);  
            entity.setGeofence_name(geofencedto.getGeofence_name());
            entity.setCoordinates(coordinateData); // Set the coordinates

            GeofenceEntity createdGeofence = geofenceRepository.save(entity);
            logger.info("Geofence created successfully with ID: {}", createdGeofence.getId());
            return createdGeofence;
        } catch (Exception e) {
            logger.error("Error occurred while creating geofence: {}", geofencedto.getGeofence_name(), e);
            throw new RuntimeException("Error creating geofence", e); // handle with appropriate exception
        }
    }


    @Override
    public List<GeofenceEntity> getAllGeofences() {
        try {
            logger.info("Fetching all geofences");
            List<GeofenceEntity> geofences = geofenceRepository.findAll();
            logger.info("Successfully fetched {} geofences", geofences.size());
            return geofences;
        } catch (Exception e) {
            logger.error("Error occurred while fetching geofences", e);
            throw new RuntimeException("Error fetching geofences", e); // handle with appropriate exception
        }
    }

    @Override
    public GeofenceEntity getGeofenceById(Long id) {
        try {
            logger.info("Fetching geofence with ID: {}", id);
            return geofenceRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Geofence with ID " + id + " not found"));
        } catch (ResourceNotFoundException e) {
            logger.error("Geofence with ID {} not found", id, e);
            throw e;  // Rethrow custom exception
        } catch (Exception e) {
            logger.error("Error occurred while fetching geofence with ID: {}", id, e);
            throw new RuntimeException("Error fetching geofence by ID", e); // handle with appropriate exception
        }
    }

  

    @Override
    public void deleteGeofence(Long id) {
        try {
            logger.info("Deleting geofence with ID: {}", id);
            GeofenceEntity geofence = geofenceRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Geofence with ID " + id + " not found"));

            geofenceRepository.delete(geofence);
            logger.info("Geofence with ID {} deleted successfully", id);
        } catch (ResourceNotFoundException e) {
            logger.error("Geofence with ID {} not found for deletion", id, e);
            throw e;  // Rethrow custom exception
        } catch (Exception e) {
            logger.error("Error occurred while deleting geofence with ID: {}", id, e);
            throw new RuntimeException("Error deleting geofence", e); // handle with appropriate exception
        }
    }


    @Override
    public GeofenceEntity updateGeofence(Long id, GeofenceDTO geofencedto) {
        try {
            logger.info("Updating geofence with ID: {}", id);

            GeofenceEntity geofenceEntity = geofenceRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Geofence not found with ID: " + id));

            geofenceEntity.setGeofence_name(geofencedto.getGeofence_name());

            List<double[]> coordinateData = new ArrayList<>();
            for (List<Double> coordinate : geofencedto.getCoordinates()) {
                double[] coordArray = new double[coordinate.size()];
                for (int i = 0; i < coordinate.size(); i++) {
                    coordArray[i] = coordinate.get(i);
                }
                coordinateData.add(coordArray); // Add the coordinate pair as a double[] to the list
            }

            List<Long> longList = new ArrayList<>();
            if (geofencedto.getAuthorizedVehicleIds() != null) {
                for (Long id1 : geofencedto.getAuthorizedVehicleIds()) {
                    longList.add(id1);
                }
            }
            geofenceEntity.setAuthorizedVehicles(longList);  // Set the list of Long values
            geofenceEntity.setCoordinates(coordinateData); // Set the coordinates

            GeofenceEntity updatedGeofence = geofenceRepository.save(geofenceEntity);

            logger.info("Geofence with ID {} updated successfully", updatedGeofence.getId());
            return updatedGeofence;  // Return the updated entity
        } catch (Exception e) {
            logger.error("Error occurred while updating geofence with ID: {}", id, e);
            throw new RuntimeException("Error updating geofence", e);  // Handle with appropriate exception
        }
    }

}

