package com.nlmk.adp.controllers;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nlmk.adp.kafka.dto.NotificationDto;
import com.nlmk.adp.services.NotificationService;

/**
 * Временный тестовый контроллер.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class TestController {

    private final NotificationService notificationService;

    /**
     * test.
     *
     * @return Authentication
     */
    @GetMapping("/test")
    @PreAuthorize("hasRole('admin')")
    public Authentication test() {
        SecurityContext context = SecurityContextHolder.getContext();
        return context.getAuthentication();
    }

    /**
     * postNotification.
     *
     * @param payload payload
     * @return ResponseEntity
     */
    @PostMapping("/notification")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> postNotification(
            @RequestBody @NotNull @Valid final NotificationDto payload
    ) {
        notificationService.sendToKafka(payload);
        return new ResponseEntity<>("\"Success\"", HttpStatus.OK);
    }

}
