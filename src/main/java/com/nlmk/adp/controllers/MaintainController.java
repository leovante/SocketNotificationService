package com.nlmk.adp.controllers;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nlmk.adp.kafka.dto.NotificationDto;
import com.nlmk.adp.kafka.dto.RoleDto;
import com.nlmk.adp.services.NotificationService;
import com.nlmk.adp.services.mapper.NotificationRoleType;
import nlmk.EnumOp;
import nlmk.Sys;
import nlmk.l3.mesadp.DbUserNotificationVer0;
import nlmk.l3.mesadp.db.user.notification.ver0.NotificationType;
import nlmk.l3.mesadp.db.user.notification.ver0.PkType;
import nlmk.l3.mesadp.db.user.notification.ver0.RecordData;

/**
 * Служебное API.
 */
@Tag(name = "Служебное API для проверки и отладки")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/maintain")
@PreAuthorize("hasRole('super-user')")
@CrossOrigin
public class MaintainController {

    private final NotificationService notificationService;

    /**
     * postNotification.
     *
     * @param payload
     *         payload
     *
     * @return ResponseEntity
     */
    @PostMapping("/notification-with-kafka")
    @Operation(
            summary = "Отправляет уведомление в kafka.",
            description = "Отправляет уведомление в kafka с дальнейшим прочтением текущим сервисом. "
                    + "Сообщение предварительно валидируется. "
                    + "Для отправки невалидных сообщений используйте kafka-rest напрямую."
    )
    public String postNotification(
            @RequestBody @NotNull @Valid final NotificationDto payload
    ) {
        notificationService.sendToKafka(payload);
        return "Success";
    }

    /**
     * 123.
     *
     * @param body
     *         123.
     */
    @SuppressWarnings("ParameterNumber")
    @Operation(summary = "Отправить тестовое уведомление супер-пользователю")
    @PostMapping("/notification-with-kafka-for-super-user")
    public void notifySuperUser(
            @RequestParam @Nullable String header,
            @RequestParam @Nullable String body,
            @RequestParam @Nullable String href
    ) {
        UUID id = UUID.randomUUID();
        var dto = new NotificationDto(
                id,
                null,
                Instant.now(),
                body == null ? "Тест текст уведомления " + id : body,
                header == null ? "Тест заголовок увеломления " + id : header,
                href == null ? "/acceptVehicles" : URI.create(href).getPath(),
                List.of(new RoleDto("super-user", NotificationRoleType.ACCEPT)),
                List.of()
        );
        notificationService.sendToKafka(dto);
    }

    /**
     * 123.
     *
     * @param body
     *         123.
     */
    @SuppressWarnings("ParameterNumber")
    @Operation(summary = "Отправка любых сообщений в kafka")
    @PostMapping("/trash-notification")
    public void sendTrash(
            @RequestParam @Nullable String ts,
            @RequestParam @Nullable EnumOp operation,
            @RequestParam @Nullable String pkTypeId,
            @RequestParam @Nullable String sysTraceId,
            @RequestParam @Nullable String header,
            @RequestParam @Nullable String body,
            @RequestParam @Nullable String href,
            @RequestParam @Nullable List<String> acceptRoles,
            @RequestParam @Nullable List<String> rejectRoles,
            @RequestParam @Nullable List<String> emails
    ) {
        var mes = new DbUserNotificationVer0(
                ts,
                operation,
                new PkType(pkTypeId),
                new Sys(-1L, sysTraceId),
                null,
                new RecordData(
                        header,
                        body,
                        href,
                        NotificationType.INFO,
                        acceptRoles == null ? List.of() : acceptRoles,
                        rejectRoles == null ? List.of() : rejectRoles,
                        emails == null ? List.of() : emails
                )
        );
        notificationService.sendTrashToKafka(mes);
    }

    /**
     * postNotification.
     *
     * @param payload
     *         payload
     *
     * @return ResponseEntity
     */
    @Operation(summary = "Отправляет уведомление напрямую.",
               description = "Отправляет уведомление всем подключенным пользователям (мимо kafka). "
                       + "Сообщение предварительно валидируется.")
    @PostMapping("/notification-broadcast")
    public String postNotificationV2(
            @RequestBody @NotNull @Valid final NotificationDto payload
    ) {
        notificationService.sendV2(payload);
        return "Success";
    }

    /**
     * postNotification.
     *
     * @param id
     *         id.
     *
     * @return ResponseEntity
     */
    @Operation(summary = "Получить информацию об уведомлении по его ID")
    @GetMapping("/notification/{id}")
    public NotificationDto getNotification(
            @PathVariable("id") UUID id
    ) {
        return notificationService.getById(id);
    }

}
