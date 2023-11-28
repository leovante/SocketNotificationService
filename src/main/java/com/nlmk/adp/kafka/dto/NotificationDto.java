package com.nlmk.adp.kafka.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * NotificationDto.
 *
 * @param uuid
 *         uuid
 * @param createdAt
 *         createdAt
 * @param updatedAt
 *         updatedAt
 * @param expiredAt
 *         expiredAt
 * @param kafkaDt
 *         kafkaDt
 * @param body
 *         body
 * @param header
 *         header
 * @param href
 *         href
 * @param roles
 *         roles
 * @param emails
 *         emails
 */
public record NotificationDto(

        UUID uuid,
        Instant createdAt,
        Instant updatedAt,
        Instant expiredAt,
        Instant kafkaDt,
        String body,
        String header,
        String href,
        List<RoleDto> roles,
        List<UserEmailDto> emails

) {
}
