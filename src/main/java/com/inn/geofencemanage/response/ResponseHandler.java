package com.inn.geofencemanage.response;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHandler {

	 public static ResponseEntity<ApiResponse> generateResponse(String message, HttpStatus status, Object responseObj) {
		 ApiResponse api=new ApiResponse();
		 api.setData(responseObj);
		 api.setMessage(message);
		 api.setStatus(status.value());

	        return new ResponseEntity<ApiResponse>(api, status);
	    }
}
