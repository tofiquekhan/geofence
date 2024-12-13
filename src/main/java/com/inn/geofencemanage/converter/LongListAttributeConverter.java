package com.inn.geofencemanage.converter;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;

public class LongListAttributeConverter implements AttributeConverter<List<Long>, String> {

	private final ObjectMapper objectMapper = new ObjectMapper();

	public String convertToDatabaseColumn(List<Long> attribute) {
		try {
			return objectMapper.writeValueAsString(attribute); // Serialize List<Long> to JSON string
		} catch (Exception e) {
			throw new IllegalArgumentException("Error converting List<Long> to JSON", e);
		}
	}

	public List<Long> convertToEntityAttribute(String dbData) {
		try {
			return objectMapper.readValue(dbData, new TypeReference<List<Long>>() {
			}); // Deserialize JSON to List<Long>
		} catch (Exception e) {
			throw new IllegalArgumentException("Error converting JSON to List<Long>", e);
		}
	}
}
