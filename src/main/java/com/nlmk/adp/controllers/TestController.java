package com.nlmk.adp.controllers;

import com.nlmk.adp.kafka.dto.NotificationDto;
import com.nlmk.adp.services.NotificationService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static org.springframework.http.ResponseEntity.ok;

/**
 * Временный тестовый контроллер.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class TestController {

    private final NotificationService notificationService;

    @GetMapping("/test")
    @PreAuthorize("hasRole('admin')")
    public Authentication test() {
        SecurityContext context = SecurityContextHolder.getContext();
        return context.getAuthentication();
    }

    @PostMapping("/notification")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> postNotification(
            @RequestBody @NotNull @Valid final NotificationDto payload
    ) {
        notificationService.send(payload);
        return ok("\"Success\"");
    }

}
