package com.nlmk.adp.kafka.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Базовая информация по уведомлению")
public record NotificationBaseDto(

        @Schema(description = "Id уведомления")
        UUID id,

        @Schema(description = "Время возникновения уведомления в системе mes")
        String happenedAt,

        @Schema(description = "Тело уведомления")
        String body,

        @Schema(description = "Заголовок уведомления")
        String header,

        @Schema(description = "Ссылка")
        String href
) {}
