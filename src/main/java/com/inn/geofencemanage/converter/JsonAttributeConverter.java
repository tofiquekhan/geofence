package com.inn.geofencemanage.converter;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class JsonAttributeConverter implements AttributeConverter<List<double[]>, String> {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(List<double[]> attribute) {
		try {
			return (attribute == null || attribute.isEmpty()) ? null : objectMapper.writeValueAsString(attribute);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error converting List<double[]> to JSON string", e);
		}
	}

	@Override
	public List<double[]> convertToEntityAttribute(String dbData) {
		try {
			return (dbData == null || dbData.isEmpty()) ? null
					: objectMapper.readValue(dbData, new TypeReference<List<double[]>>() {
					});
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error converting JSON string to List<double[]>", e);
		}
	}
}
