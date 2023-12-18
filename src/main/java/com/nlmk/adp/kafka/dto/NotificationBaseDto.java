package com.nlmk.adp.kafka.dto;

import java.time.Instant;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;

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

)       implements Comparable<NotificationBaseDto> {
    @Override
    public int compareTo(@NotNull NotificationBaseDto o) {
        return Instant.parse(this.happenedAt).compareTo(Instant.parse(o.happenedAt));
    }

}
