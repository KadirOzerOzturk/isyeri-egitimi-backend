package com.isyeriegitimi.backend.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isyeriegitimi.backend.security.enums.Role;
import java.util.List;

@Converter
public class RoleListJsonConverter implements AttributeConverter<List<Role>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Role> roles) {
        try {
            return objectMapper.writeValueAsString(roles);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting Role list to JSON", e);
        }
    }

    @Override
    public List<Role> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, objectMapper.getTypeFactory().constructCollectionType(List.class, Role.class));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting JSON to Role list", e);
        }
    }
}
