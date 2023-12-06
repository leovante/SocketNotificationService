package com.nlmk.adp.kafka.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.lang.Nullable;

import com.nlmk.adp.dto.NotificationCheck;

/**
 * Дто с уведомлением.
 *
 * @param id
 *         id из кафки, считается глобальным id уведомления.
 * @param expiredAt
 *         expiredAt
 * @param happenedAt
 *         время возникновения уведомления.
 * @param body
 *         тело уведомления.
 * @param header
 *         заголовок уведомления.
 * @param href
 *         ссылка, на которую произойдет переход при нажатии на уведомление.
 * @param roles
 *         список ролей для маршрутизации уведомления.
 * @param emails
 *         список emails для маршрутизации уведомления.
 */
@NotificationCheck
public record NotificationDto(

        UUID id,
        @Nullable Instant expiredAt,
        Instant happenedAt,
        String body,
        String header,
        String href,
        List<RoleDto> roles,
        List<UserEmailDto> emails

) {
}
