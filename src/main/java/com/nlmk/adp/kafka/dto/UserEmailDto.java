package com.nlmk.adp.kafka.dto;

import java.time.OffsetDateTime;

public record UserEmailDto(
        String email,
        OffsetDateTime readAt
) {
}
