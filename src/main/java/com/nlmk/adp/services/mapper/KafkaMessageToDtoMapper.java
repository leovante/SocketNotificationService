package com.nlmk.adp.services.mapper;

import java.net.URI;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.nlmk.adp.kafka.dto.NotificationDto;
import com.nlmk.adp.kafka.dto.ReadByUserEmailDto;
import com.nlmk.adp.kafka.dto.RoleDto;
import com.nlmk.adp.util.SpringMapperConfig;
import nlmk.l3.mesadp.DbUserNotificationVer0;
import nlmk.l3.mesadp.db.user.notification.ver0.RecordData;

/**
 * Сообщения из kafka в дто.
 */
@Mapper(config = SpringMapperConfig.class, uses = {KafkaDateMapper.class})
public interface KafkaMessageToDtoMapper {

    int DEFAULT_LIFETIME_MINUTES = 1440;

    /**
     * Сообщение с уведомлением.
     *
     * @param req
     *         kafka mes.
     *
     * @return dto.
     */
    @Mapping(target = "id", source = "pk.id")
    @Mapping(target = "header", source = "data.header", qualifiedByName = "strip")
    @Mapping(target = "body", source = "data.body", qualifiedByName = "strip")
    @Mapping(target = "href", source = "data.href", qualifiedByName = "calcHref")
    @Mapping(target = "roles", expression = "java(mapRolesToDto(req.getData()))")
    @Mapping(target = "readByUserEmails", expression = "java(mapEmailsToDto(req.getData().getAcceptEmails()))")
    @Mapping(target = "happenedAt", source = "ts")
    @Mapping(target = "expiredAt", source = "ts", qualifiedByName = "calcExpiredAt")
    @Mapping(target = "ordinalNumber", ignore = true)
    NotificationDto mapDataToDto(DbUserNotificationVer0 req);

    /**
     * Чистит строку.
     *
     * @param raw
     *         исходная строка.
     *
     * @return очищенная строка.
     */
    @Named("strip")
    default String strip(String raw) {
        if (raw == null) {
            return "";
        } else {
            return raw.strip();
        }
    }

    /**
     * Вытаскивает путь для перехода. Фронт ожидает строку вида /qwer1/123.
     *
     * @param raw
     *         исходная ссылка/путь.
     *
     * @return нужным нам путь.
     */
    @Named("calcHref")
    default String calcHref(String raw) {
        return URI.create(raw).getPath();
    }

    /**
     * Расчет времени актуальности уведомления.
     *
     * @param notificationDt
     *         время уведомления.
     *
     * @return время окончания актуальности.
     */
    @Named("calcExpiredAt")
    default Instant calcExpiredAt(String notificationDt) {
        var temporal = KafkaDateMapper.KAFKA_TO_DATE_TIME_FORMATTER.parse(notificationDt.strip());
        return Instant.from(temporal).plus(DEFAULT_LIFETIME_MINUTES, ChronoUnit.MINUTES);
    }

    /**
     * Парсинг ролей.
     *
     * @param data
     *         данные из уведомления.
     *
     * @return список ролей по типу.
     */
    default List<RoleDto> mapRolesToDto(RecordData data) {
        var accRoles = CollectionUtils.emptyIfNull(data.getAcceptRoles());
        var rejRoles = CollectionUtils.emptyIfNull(data.getRejectRoles());

        return Stream.concat(
                accRoles.stream()
                        .filter(it -> it != null && !it.isBlank())
                        .map(i -> new RoleDto(i.strip(), NotificationRoleType.ACCEPT)),
                rejRoles.stream()
                        .filter(it -> it != null && !it.isBlank())
                        .map(i -> new RoleDto(i.strip(), NotificationRoleType.REJECT))
        ).toList();
    }

    /**
     * Парсинг списка email.
     *
     * @param emails
     *         readByUserEmails as strings.
     *
     * @return readByUserEmails.
     */
    default List<ReadByUserEmailDto> mapEmailsToDto(List<String> emails) {
        return emails.stream()
                     .filter(it -> !it.isBlank())
                     .map(i -> new ReadByUserEmailDto(i, null))
                     .toList();
    }

    /**
     * Парсинг ИД.
     *
     * @param uuid
     *         ид как текст.
     *
     * @return ид как UUID.
     */
    default UUID stringToUuid(String uuid) {
        return UUID.fromString(uuid);
    }

}
