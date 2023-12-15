package com.nlmk.adp.kafka.dto;

import java.time.Instant;

import org.springframework.lang.Nullable;

/**
 * Дто с описанием почты для уведомления.
 *
 * @param email
 *         email.
 * @param readAt
 *         дата прочтения.
 */
public record EmailDto(
        String email,
        @Nullable Instant readAt
) {
}
