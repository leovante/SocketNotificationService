package com.nlmk.adp.services.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.nlmk.adp.db.entity.NotificationEntity;
import com.nlmk.adp.db.entity.NotificationRolesEntity;
import com.nlmk.adp.db.entity.NotificationUserSuccessEntity;
import com.nlmk.adp.kafka.dto.NotificationBaseDto;
import com.nlmk.adp.kafka.dto.NotificationDto;
import com.nlmk.adp.kafka.dto.RoleDto;
import com.nlmk.adp.kafka.dto.UserEmailDto;
import com.nlmk.adp.util.SpringMapperConfig;

/**
 * NotificationFromDaoMapper.
 */
@Mapper(config = SpringMapperConfig.class)
public interface NotificationToDtoMapper {

    /**
     * map to dto.
     *
     * @param entity
     *         entity.
     *
     * @return dto.
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "header", source = "header")
    @Mapping(target = "body", source = "body")
    @Mapping(target = "href", source = "href")
    @Mapping(target = "roles", source = "notificationRolesEntities")
    @Mapping(target = "emails", source = "notificationUserSuccessEntities")
    @Mapping(target = "happenedAt", source = "kafkaDt")
    @Mapping(target = "expiredAt", source = "expiredAt")
    NotificationDto mapToDto(NotificationEntity entity);

    /**
     * map to dto.
     *
     * @param entity
     *         entity.
     *
     * @return NotificationBaseDto.
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "header", source = "header")
    @Mapping(target = "body", source = "body")
    @Mapping(target = "href", source = "href")
    @Mapping(target = "happenedAt", source = "kafkaDt")
    NotificationBaseDto mapToBaseDto(NotificationEntity entity);

    /**
     * role.
     *
     * @param role
     *
     * @return role.
     */
    @Mapping(target = "role", source = "primaryKey.role")
    @Mapping(target = "roleType", source = "roleType")
    RoleDto mapRole(NotificationRolesEntity role);

    /**
     * email.
     *
     * @param email
     *
     * @return email.
     */
    @Mapping(target = "email", source = "primaryKey.email")
    @Mapping(target = "readAt", source = "readAt")
    UserEmailDto mapEmail(NotificationUserSuccessEntity email);

}
