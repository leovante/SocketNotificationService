package com.nlmk.adp.kafka.dto;

import java.time.Instant;

import org.springframework.lang.Nullable;

/**
 * Дто с прочтением уведомления пользователем.
 *
 * @param email
 *         email, идентифицирующий пользователя.
 * @param readAt
 *         дата прочтения. Если null - не прочтено.
 */
public record ReadByUserEmailDto(
        String email,
        @Nullable Instant readAt
) {
}
