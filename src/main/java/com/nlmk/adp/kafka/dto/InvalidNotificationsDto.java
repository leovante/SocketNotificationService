package com.nlmk.adp.kafka.dto;

import java.time.OffsetDateTime;

public record InvalidNotificationsDto(
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        String rawMessage,
        String errorMessage
) {
}
