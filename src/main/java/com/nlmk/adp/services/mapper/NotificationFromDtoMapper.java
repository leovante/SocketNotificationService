package com.nlmk.adp.services.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.nlmk.adp.kafka.dto.NotificationDto;
import com.nlmk.adp.kafka.dto.RoleDto;
import com.nlmk.adp.kafka.dto.UserEmailDto;
import com.nlmk.adp.util.SpringMapperConfig;
import nlmk.l3.mesadp.DbUserNotificationVer0;

/**
 * NotificationFromDtoMapper.
 */
@Mapper(config = SpringMapperConfig.class, uses = {DateMapper.class})
public interface NotificationFromDtoMapper {

    /**
     * mapDataFromDto.
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
             expression = "java(mapRolesFromDto(notificationDto, com.nlmk.adp.services.mapper.RoleStatus.ACCEPT))")
    @Mapping(target = "data.rejectRoles",
             expression = "java(mapRolesFromDto(notificationDto, com.nlmk.adp.services.mapper.RoleStatus.REJECT))")
    @Mapping(target = "data.acceptEmails",
             expression = "java(mapEmailsFromDto(notificationDto))")
    @Mapping(target = "metadata.kafkaTimestamp", source = "kafkaDt")
    @Mapping(target = "ts", source = "kafkaDt")
    @Mapping(target = "op", constant = "I")
    @Mapping(target = "pk", ignore = true)
    @Mapping(target = "sys", ignore = true)
    DbUserNotificationVer0 mapDataFromDto(NotificationDto notificationDto);

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
    default List<String> mapRolesFromDto(NotificationDto data, RoleStatus type) {
        var roles = data.roles();
        return roles.stream()
                    .filter(i -> i.roleType().equals(type.toString()))
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
        return dto.emails().stream().map(UserEmailDto::email).collect(Collectors.toList());
    }

}
