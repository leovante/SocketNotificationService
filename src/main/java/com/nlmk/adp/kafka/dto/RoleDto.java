package com.nlmk.adp.kafka.dto;

import com.nlmk.adp.services.mapper.NotificationRoleType;

/**
 * Дто с описанием роли для уведомления.
 *
 * @param role
 *         имя роли.
 * @param roleType
 *         тип роли.
 */
public record RoleDto(
        String role,
        NotificationRoleType roleType
) {
}
