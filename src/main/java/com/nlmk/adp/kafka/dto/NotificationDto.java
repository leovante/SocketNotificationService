package com.nlmk.adp.kafka.dto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

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
