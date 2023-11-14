package com.nlmk.adp.db.entity.convert;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
@RequiredArgsConstructor
public class RawMessageConverter implements AttributeConverter<JsonNode, String> {

    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(JsonNode attribute) {
        if (attribute == null) {
            return null;
        }
        return objectMapper.writeValueAsString(attribute);
    }

    @SneakyThrows
    @Override
    public JsonNode convertToEntityAttribute(String dbData) {
        if (isEmpty(dbData)) {
            return null;
        }
        return objectMapper.readTree(dbData);
    }
}
