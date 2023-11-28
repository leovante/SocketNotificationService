package com.nlmk.adp.services.mapper;

import java.time.Instant;

import org.mapstruct.Mapper;

import com.nlmk.adp.util.SpringMapperConfig;

/**
 * DateMapper.
 */
@Mapper(config = SpringMapperConfig.class)
public abstract class DateMapper {

    /**
     * mapStringTimestampToOffsetDateTime.
     *
     * @param timestamp
     *         timestamp
     *
     * @return OffsetDateTime
     */
    public Instant mapStringTimestampToOffsetDateTime(String timestamp) {
        return Instant.now();
    }

    /**
     * mapOffsetDateTimeToStringTimestamp.
     *
     * @param timestamp
     *         timestamp
     *
     * @return String
     */
    public String mapOffsetDateTimeToStringTimestamp(Instant timestamp) {
        return Instant.now().toString();
    }

}
