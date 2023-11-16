package com.nlmk.adp.services.mapper;

import com.nlmk.adp.db.entity.*;
import com.nlmk.adp.kafka.dto.NotificationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface NotificationToDaoMapper {

    @Mapping(target = "kafkaDt", source = "kafkaDt")
    @Mapping(target = "notificationRolesEntities", expression = "java(mapRolesToEntity(dto))")
    @Mapping(target = "notificationUserSuccessEntities", expression = "java(mapEmailsToEntity(dto))")
    NotificationEntity mapDtoToEntity(NotificationDto dto);

    default Set<NotificationRolesEntity> mapRolesToEntity(NotificationDto notificationDto) {
        return notificationDto.roles().stream()
                .map(dto -> {
                    var entity = new NotificationRolesEntity();
                    var role = new NotificationRolesPk();
                    role.setRole(dto.role());

                    entity.setPrimaryKey(role);
                    entity.setRoleType(dto.roleType());
                    return entity;
                })
                .collect(Collectors.toSet());
    }

    default Set<NotificationUserSuccessEntity> mapEmailsToEntity(NotificationDto notificationDto) {
        return notificationDto.emails().stream()
                .map(dto -> {
                    var entity = new NotificationUserSuccessEntity();
                    var emailPk = new NotificationEmailPk();
                    emailPk.setEmail(dto.email());

                    entity.setPrimaryKey(emailPk);
                    return entity;
                })
                .collect(Collectors.toSet());
    }
}
