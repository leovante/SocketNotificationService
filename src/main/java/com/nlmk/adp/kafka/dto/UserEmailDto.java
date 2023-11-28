package com.nlmk.adp.kafka.dto;

import java.time.Instant;

public record UserEmailDto(
        String email,
        Instant readAt
) {
}
