package com.nlmk.adp.controllers;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nlmk.adp.kafka.dto.NotificationBaseDto;
import com.nlmk.adp.services.NotificationService;

/**
 * API уведомлений.
 */
@Tag(name = "API уведомлений")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/notification")
@CrossOrigin
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * getNotifications.
     *
     * @param limit
     *         limit.
     *
     * @return List of NotificationBaseDto
     */
    @GetMapping("/backlog")
    @Operation(
            summary = "Получение списка уведомлений",
            description = "Позволяет получить список уведомлений согласно ролям и email пользователя из токена"
    )
    public List<NotificationBaseDto> getNotificationsBacklog(
            @Parameter(description = "Максимальное количество уведовлений в списке")
            @RequestParam(value = "limit", defaultValue = "1000") Integer limit
    ) {
        return notificationService.getBacklogNotificationsForCurrentUser(limit);
    }

}
