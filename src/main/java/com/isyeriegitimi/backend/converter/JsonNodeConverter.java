package com.isyeriegitimi.backend.converter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.postgresql.util.PGobject;
@Converter(autoApply = false)
public class JsonNodeConverter implements AttributeConverter<JsonNode, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(JsonNode jsonNode) {
        try {
            return jsonNode != null ? objectMapper.writeValueAsString(jsonNode) : null;
        } catch (Exception e) {
            throw new RuntimeException("JSON serialization failed", e);
        }
    }

    @Override
    public JsonNode convertToEntityAttribute(String dbData) {
        try {
            return (dbData != null && !dbData.isEmpty()) ? objectMapper.readTree(dbData) : null;
        } catch (Exception e) {
            throw new RuntimeException("JSON deserialization failed", e);
        }
    }
}


