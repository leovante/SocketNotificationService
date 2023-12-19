package com.nlmk.adp.kafka.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.Nullable;

import com.nlmk.adp.dto.NotificationCheck;

/**
 * Дто с уведомлением.
 *
 * @param id
 *         id из кафки, считается глобальным id уведомления.
 * @param expiredAt
 *         expiredAt - после этой даты уведомление не будет доставлено пользователю.
 * @param happenedAt
 *         время возникновения уведомления в системе mes.
 * @param body
 *         тело уведомления - не пустое, не более 500 символов.
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

        @Schema(description = "Глобальный идентификатор уведомления.")
        UUID id,

        @Schema(description = "Уведомление не будет отправлено после этой даты.")
        @Nullable Instant expiredAt,

        @Schema(description = "Дата создания")
        Instant happenedAt,

        @Schema(description = "Тело, не пустое и не более 500 символов.")
        String body,

        @Schema(description = "Заголовок, не пустое и не более 500 символов.")
        String header,

        @Schema(description = "Путь для перехода вида /qwer/asdf, не пустое и не более 500 символов.")
        String href,

        @Schema(description = "Список ролей для маршрутизации уведомления.")
        List<RoleDto> roles,

        @Schema(description = "Список email-идентификаторов пользователей для маршрутизации уведомления.")
        List<EmailDto> emails,

        @Schema(description = "Порядковый номер прихода уведомления")
        Long ordinalNumber

) {
}
