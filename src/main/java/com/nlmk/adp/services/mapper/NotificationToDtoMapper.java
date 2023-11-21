package com.nlmk.adp.services.mapper;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.nlmk.adp.kafka.dto.NotificationDto;
import com.nlmk.adp.kafka.dto.RoleDto;
import com.nlmk.adp.kafka.dto.UserEmailDto;
import nlmk.l3.mesadp.DbUserNotificationVer0;
import nlmk.l3.mesadp.db.user.notification.ver0.RecordData;

/**
 * NotificationToDtoMapper.
 */
@Mapper(componentModel = "spring", uses = {DateMapper.class})
public interface NotificationToDtoMapper {

    /**
     * mapDataToDto.
     *
     * @param req
     *         req
     *
     * @return NotificationDto
     */
    @Mapping(target = "header", source = "data.header")
    @Mapping(target = "body", source = "data.body")
    @Mapping(target = "href", source = "data.href")
    @Mapping(target = "roles", expression = "java(mapRolesToDto(req.getData()))")
    @Mapping(target = "emails", expression = "java(mapEmailsToDto(req.getData().getAcceptEmails()))")
    @Mapping(target = "kafkaDt", source = "metadata.kafkaTimestamp")
    NotificationDto mapDataToDto(DbUserNotificationVer0 req);

    /**
     * mapRolesToDto.
     *
     * @param data
     *         data
     *
     * @return List
     */
    default List<RoleDto> mapRolesToDto(RecordData data) {
        var accRoles = data.getAcceptRoles();
        var rejRoles = data.getRejectRoles();

        return Stream.concat(
                accRoles.stream().map(i -> new RoleDto(i, RoleStatus.ACCEPT.toString())),
                rejRoles.stream().map(i -> new RoleDto(i, RoleStatus.REJECT.toString()))
        ).collect(Collectors.toList());
    }

    /**
     * mapEmailsToDto.
     *
     * @param emails
     *         emails
     *
     * @return List
     */
    default List<UserEmailDto> mapEmailsToDto(List<String> emails) {
        return emails.stream().map(i -> new UserEmailDto(i, null)).collect(Collectors.toList());
    }

}
