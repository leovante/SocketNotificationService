package com.nlmk.adp.services.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.nlmk.adp.db.entity.NotificationEmailPk;
import com.nlmk.adp.db.entity.NotificationEntity;
import com.nlmk.adp.db.entity.NotificationRolesEntity;
import com.nlmk.adp.db.entity.NotificationRolesPk;
import com.nlmk.adp.db.entity.NotificationUserSuccessEntity;
import com.nlmk.adp.kafka.dto.NotificationDto;

/**
 * NotificationToDaoMapper.
 */
@Mapper(componentModel = "spring")
public interface NotificationToDaoMapper {

    /**
     * mapDtoToEntity.
     *
     * @param dto dto
     * @return NotificationEntity
     */
    @Mapping(target = "id", source = "uuid")
    @Mapping(target = "kafkaDt", source = "kafkaDt")
    @Mapping(target = "notificationRolesEntities", expression = "java(mapRolesToEntity(dto))")
    @Mapping(target = "notificationUserSuccessEntities", expression = "java(mapEmailsToEntity(dto))")
    NotificationEntity mapDtoToEntity(NotificationDto dto);

    /**
     * mapRolesToEntity.
     *
     * @param notificationDto notificationDto
     * @return Set
     */
    default Set<NotificationRolesEntity> mapRolesToEntity(NotificationDto notificationDto) {
        return notificationDto.roles().stream()
                .map(dto -> {
                    var entity = new NotificationRolesEntity();
                    var role = new NotificationRolesPk();
                    role.setRole(dto.role());
                    role.setNotificationId(notificationDto.uuid());

                    entity.setPrimaryKey(role);
                    entity.setRoleType(dto.roleType());
                    return entity;
                })
                .collect(Collectors.toSet());
    }

    /**
     * mapEmailsToEntity.
     *
     * @param notificationDto notificationDto
     * @return Set
     */
    default Set<NotificationUserSuccessEntity> mapEmailsToEntity(NotificationDto notificationDto) {
        return notificationDto.emails().stream()
                .map(dto -> {
                    var entity = new NotificationUserSuccessEntity();
                    var emailPk = new NotificationEmailPk();
                    emailPk.setEmail(dto.email());
                    emailPk.setNotificationId(notificationDto.uuid());

                    entity.setPrimaryKey(emailPk);
                    return entity;
                })
                .collect(Collectors.toSet());
    }

}
