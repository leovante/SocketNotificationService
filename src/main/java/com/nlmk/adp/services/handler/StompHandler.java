package com.nlmk.adp.services.handler;

import java.security.Principal;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

/**
 * StompHandler.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler {

    private final SimpMessagingTemplate messagingTemplate;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Value("${websocket.topic.log:/topic/log}")
    private String logTopic;

    /**
     * ConnectEvent.
     *
     * @param <S> S
     */
    @Component
    class ConnectEvent<S> implements ApplicationListener<SessionConnectEvent> {

        @Override
        public void onApplicationEvent(SessionConnectEvent event) {
            String id = Optional.ofNullable(event.getUser())
                    .map(Principal::getName)
                    .orElse(null);

            if (id == null) {
                log.info("No user event to connect");
                return;
            }

            messagingTemplate.convertAndSend(
                    logTopic,
                    "Connect session for user: " + event.getUser().getName());

            log.info("stomp session connected");
        }

    }

    /**
     * SubscribeEvent.
     *
     * @param <S> S
     */
    @Component
    class SubscribeEvent<S> implements ApplicationListener<SessionSubscribeEvent> {

        @Override
        public void onApplicationEvent(SessionSubscribeEvent event) {
            String id = Optional.ofNullable(event.getUser())
                    .map(Principal::getName)
                    .orElse(null);

            if (id == null) {
                log.info("No user event to subscribe");
                return;
            }

            messagingTemplate.convertAndSend(
                    logTopic,
                    String.format("Subscribe for user: %s", event.getUser().getName()));

            applicationEventPublisher.publishEvent(event.getUser());

            log.info("Subscribe user {}", event.getUser().getName());
        }

    }

    /**
     * DisconnectEvent.
     *
     * @param <S> S
     */
    @Component
    class DisconnectEvent<S> implements ApplicationListener<SessionDisconnectEvent> {

        @Override
        public void onApplicationEvent(SessionDisconnectEvent event) {
            String id = Optional.ofNullable(event.getUser())
                    .map(Principal::getName)
                    .orElse("");

            if (id == null) {
                log.info("No user event to disconnect");
                return;
            }

            messagingTemplate.convertAndSend(
                    logTopic,
                    "Disconnect session for user: " + event.getUser().getName());

            log.info("stomp session disconnected");
        }

    }

}
