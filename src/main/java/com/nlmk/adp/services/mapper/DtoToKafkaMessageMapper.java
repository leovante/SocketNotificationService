package com.nlmk.adp.services.mapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.nlmk.adp.kafka.dto.EmailDto;
import com.nlmk.adp.kafka.dto.NotificationDto;
import com.nlmk.adp.kafka.dto.RoleDto;
import com.nlmk.adp.util.SpringMapperConfig;
import nlmk.l3.mesadp.DbUserNotificationVer0;

/**
 * Служебный маппер для формирования kafka сообщений из дто.
 */
@Mapper(config = SpringMapperConfig.class, uses = {KafkaDateMapper.class})
public interface DtoToKafkaMessageMapper {

    /**
     * Сообщение с уведомлением.
     *
     * @param notificationDto
     *         notificationDto
     *
     * @return DbUserNotificationVer0
     */
    @Mapping(target = "data.header", source = "header")
    @Mapping(target = "data.body", source = "body")
    @Mapping(target = "data.href", source = "href")
    @Mapping(target = "data.acceptRoles",
             expression = "java(mapRolesFromDto(notificationDto, NotificationRoleType.ACCEPT))")
    @Mapping(target = "data.rejectRoles",
             expression = "java(mapRolesFromDto(notificationDto, NotificationRoleType.REJECT))")
    @Mapping(target = "data.acceptEmails",
             expression = "java(mapEmailsFromDto(notificationDto))")
    @Mapping(target = "data.type", constant = "INFO")
    @Mapping(target = "metadata.kafkaTimestamp", source = "happenedAt")
    @Mapping(target = "metadata.kafkaKey", source = "id")
    @Mapping(target = "metadata.kafkaTopic", source = "id")
    @Mapping(target = "ts", source = "happenedAt")
    @Mapping(target = "op", constant = "I")
    @Mapping(target = "pk", source = "id")
    @Mapping(target = "sys", source = "id")
    DbUserNotificationVer0 mapDataFromDto(NotificationDto notificationDto);

    /**
     * 123.
     *
     * @param id
     *         123.
     *
     * @return 123.
     */
    default nlmk.l3.mesadp.db.user.notification.ver0.PkType mapToPk(UUID id) {
        return nlmk.l3.mesadp.db.user.notification.ver0.PkType
                .newBuilder()
                .setId(id.toString())
                .build();
    }

    /**
     * 123.
     *
     * @param id
     *         123.
     *
     * @return 123.
     */
    default nlmk.Sys maptoSys(UUID id) {
        return nlmk.Sys
                .newBuilder()
                .setSeqID(1)
                .setTraceID(UUID.randomUUID().toString())
                .build();
    }

    /**
     * mapRolesFromDto.
     *
     * @param data
     *         data
     * @param type
     *         type
     *
     * @return List
     */
    default List<String> mapRolesFromDto(NotificationDto data, NotificationRoleType type) {
        var roles = data.roles();
        return roles.stream()
                    .filter(i -> type == i.roleType())
                    .map(RoleDto::role)
                    .collect(Collectors.toList());
    }

    /**
     * mapEmailsFromDto.
     *
     * @param dto
     *         dto
     *
     * @return List
     */
    default List<String> mapEmailsFromDto(NotificationDto dto) {
        return dto.emails().stream().map(EmailDto::email).collect(Collectors.toList());
    }

}
