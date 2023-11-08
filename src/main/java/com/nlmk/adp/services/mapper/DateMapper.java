package com.nlmk.adp.services.mapper;

import org.mapstruct.Mapper;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import static org.springframework.util.StringUtils.isEmpty;

@Mapper
public abstract class DateMapper {

    public OffsetDateTime mapStringTimestampToOffsetDateTime(String timestamp) {
        return OffsetDateTime.now();
    }

}
