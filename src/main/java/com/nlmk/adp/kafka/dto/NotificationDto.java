package com.nlmk.adp.kafka.dto;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * NotificationDto.
 *
 * @param createdAt createdAt
 * @param updatedAt updatedAt
 * @param expiredAt expiredAt
 * @param kafkaDt kafkaDt
 * @param body body
 * @param header header
 * @param href href
 * @param roles roles
 * @param emails emails
 */
public record NotificationDto(

        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        OffsetDateTime expiredAt,
        OffsetDateTime kafkaDt,
        String body,
        String header,
        String href,
        List<RoleDto> roles,
        List<UserEmailDto> emails

) {
}
