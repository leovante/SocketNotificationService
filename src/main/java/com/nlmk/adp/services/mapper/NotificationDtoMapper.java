package com.nlmk.adp.services.mapper;

import com.nlmk.adp.dto.DbUserNotificationVer0;
import com.nlmk.adp.kafka.dto.NotificationDto;
import com.nlmk.adp.kafka.dto.RoleDto;
import com.nlmk.adp.kafka.dto.UserEmailDto;
import nlmk.l3.mesadp.db.user.notification.ver0.RecordData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper(componentModel = "spring", uses = {DateMapper.class})
public interface NotificationDtoMapper {

    @Mapping(target = "header", source = "data.header")
    @Mapping(target = "body", source = "data.body")
    @Mapping(target = "href", source = "data.href")
    @Mapping(target = "roles", expression = "java(mapRolesToDto(req.getData()))")
    @Mapping(target = "emails", expression = "java(mapEmailsToDto(req.getData().getAcceptEmails()))")
    @Mapping(target = "kafkaDt", source = "metadata.kafkaTimestamp")
    NotificationDto mapDataToDto(DbUserNotificationVer0 req);

    default List<RoleDto> mapRolesToDto(RecordData data) {
        var accRoles = data.getAcceptRoles();
        var rejRoles = data.getRejectRoles();

        return Stream.concat(
                accRoles.stream().map(i -> new RoleDto(i, "accept")),
                rejRoles.stream().map(i -> new RoleDto(i, "reject"))
        ).collect(Collectors.toList());
    }

    default List<UserEmailDto> mapEmailsToDto(List<String> emails) {
        return emails.stream().map(i -> new UserEmailDto(i, null)).collect(Collectors.toList());
    }

}
