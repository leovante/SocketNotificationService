package com.nlmk.adp.services.mapper;

import java.time.OffsetDateTime;

import org.mapstruct.Mapper;

/**
 * DateMapper.
 */
@Mapper
public abstract class DateMapper {

    /**
     * mapStringTimestampToOffsetDateTime.
     *
     * @param timestamp timestamp
     * @return OffsetDateTime
     */
    public OffsetDateTime mapStringTimestampToOffsetDateTime(String timestamp) {
        return OffsetDateTime.now();
    }

    /**
     * mapOffsetDateTimeToStringTimestamp.
     *
     * @param timestamp timestamp
     * @return String
     */
    public String mapOffsetDateTimeToStringTimestamp(OffsetDateTime timestamp) {
        return OffsetDateTime.now().toString();
    }

}
