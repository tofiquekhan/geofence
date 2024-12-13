package com.inn.geofencemanage.exception;

import com.inn.geofencemanage.response.ApiResponse;
import com.inn.geofencemanage.response.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// Handle ResourceNotFoundException
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
		return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.NOT_FOUND, null);

	}

	// Handle ValidationException
	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ApiResponse> handleValidationException(ValidationException ex) {
		return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, null);

	}

	// Handle all other Exceptions
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse> handleGenericException(Exception ex) {
		return ResponseHandler.generateResponse("An unexpected error occurred: " + ex.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR, null);

	}
}
