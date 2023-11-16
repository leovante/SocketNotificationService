package com.nlmk.adp.services.mapper;

import org.mapstruct.Mapper;

import java.time.OffsetDateTime;

@Mapper
public abstract class DateMapper {

    public OffsetDateTime mapStringTimestampToOffsetDateTime(String timestamp) {
        return OffsetDateTime.now();
    }

    public String mapOffsetDateTimeToStringTimestamp(OffsetDateTime timestamp) {
        return OffsetDateTime.now().toString();
    }

}
