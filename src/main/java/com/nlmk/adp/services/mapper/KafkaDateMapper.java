package com.nlmk.adp.services.mapper;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.mapstruct.Mapper;

import com.nlmk.adp.util.DateFormatConstants;
import com.nlmk.adp.util.SpringMapperConfig;

/**
 * Форматтер дат для кафки.
 */
@Mapper(config = SpringMapperConfig.class)
public abstract class KafkaDateMapper {

    public static final DateTimeFormatter KAFKA_TO_DATE_TIME_FORMATTER = DateTimeFormatter
            .ofPattern(DateFormatConstants.KAFKA_DATE_TIME_PATTERNS);

    public static final DateTimeFormatter KAFKA_DATE_TIME_FORMATTER = DateTimeFormatter
            .ofPattern(DateFormatConstants.KAFKA_DATE_TIME_PATTERN);

    /**
     * Из строки для кафки.
     *
     * @param timestamp
     *         timestamp
     *
     * @return OffsetDateTime
     */
    public Instant maptoInstant(String timestamp) {
        var temporal = KAFKA_TO_DATE_TIME_FORMATTER.parse(timestamp.strip());
        return Instant.from(temporal);
    }

    /**
     * В строку для кафки.
     *
     * @param timestamp
     *         инстант.
     *
     * @return строковое представление для кафки.
     */
    public String mapInstantTOString(Instant timestamp) {
        return KAFKA_DATE_TIME_FORMATTER.format(timestamp.atOffset(ZoneOffset.UTC));
    }

}
