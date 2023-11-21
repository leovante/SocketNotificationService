package com.nlmk.adp.db.entity.convert;

import javax.persistence.AttributeConverter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * RawMessageConverter.
 */
@Component
@RequiredArgsConstructor
public class RawMessageConverter implements AttributeConverter<JsonNode, String> {

    private final ObjectMapper objectMapper;

    /**
     * convertToDatabaseColumn.
     *
     * @param attribute  the entity attribute value to be converted
     * @return String
     */
    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(JsonNode attribute) {
        if (attribute == null) {
            return null;
        }
        return objectMapper.writeValueAsString(attribute);
    }

    /**
     * convertToEntityAttribute.
     *
     * @param dbData  the data from the database column to be
     *                converted
     * @return JsonNode
     */
    @SneakyThrows
    @Override
    public JsonNode convertToEntityAttribute(String dbData) {
        if (ObjectUtils.isEmpty(dbData)) {
            return null;
        }
        return objectMapper.readTree(dbData);
    }

}
