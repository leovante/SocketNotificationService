package com.nlmk.adp.services.mapper;

import com.nlmk.adp.dto.DbUserNotificationVer0;
import com.nlmk.adp.kafka.dto.NotificationDto;
import com.nlmk.adp.kafka.dto.RoleDto;
import com.nlmk.adp.kafka.dto.UserEmailDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {DateMapper.class})
public interface NotificationFromDtoMapper {

    @Mapping(target = "data.header", source = "header")
    @Mapping(target = "data.body", source = "body")
    @Mapping(target = "data.href", source = "href")
    @Mapping(target = "data.acceptRoles", expression = "java(mapRolesFromDto(notificationDto, com.nlmk.adp.services.mapper.RoleStatus.ACCEPT))")
    @Mapping(target = "data.rejectRoles", expression = "java(mapRolesFromDto(notificationDto, com.nlmk.adp.services.mapper.RoleStatus.REJECT))")
    @Mapping(target = "data.acceptEmails", expression = "java(mapEmailsFromDto(notificationDto))")
    @Mapping(target = "metadata.kafkaTimestamp", source = "kafkaDt")
    DbUserNotificationVer0 mapDataFromDto(NotificationDto notificationDto);


    default List<String> mapRolesFromDto(NotificationDto data, RoleStatus type) {
        var roles = data.roles();
        return roles.stream()
                .filter(i -> i.roleType().equals(type.toString()))
                .map(RoleDto::role)
                .collect(Collectors.toList());
    }

    default List<String> mapEmailsFromDto(NotificationDto dto) {
        return dto.emails().stream().map(UserEmailDto::email).collect(Collectors.toList());
    }

}
