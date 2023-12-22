package com.nlmk.adp.services.component;

import java.security.Principal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.nlmk.adp.dto.JwtAuthentication;
import com.nlmk.adp.kafka.dto.NotificationDto;
import com.nlmk.adp.services.SocketMessageSenderService;

/**
 * SocketEventPublisher.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SocketEventPublisher {

    private final SocketMessageSenderService socketMessageSenderService;

    /**
     * send.
     *
     * @param body body
     */
    @EventListener
    @Async
    public void send(NotificationDto body) {
        socketMessageSenderService.send(body);
        log.info("Handle notification to publish, uuid: {}", body.id());
    }

    /**
     * send.
     *
     * @param user user
     */
    @EventListener
    @Async
    public void send(Principal user) {
        if (user instanceof JwtAuthentication) {
            socketMessageSenderService.resendToNotReadedWsUsers((JwtAuthentication) user);
        }
        log.info("Handle notification to publish, uuid: {}", user.getName());
    }

}
