package com.nlmk.adp.services.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.session.MapSession;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Slf4j
@Component
public class StompHandler {

    private final SessionRepository<MapSession> repository;
    private final SimpMessageSendingOperations messagingTemplate;

    @Value("${websocket.topic.start:/topic/hello}")
    private String START_TOPIC;

    /**
     * StompHandler.
     *
     * @param messagingTemplate messagingTemplate
     * @param repository        repository
     */
    public StompHandler(SimpMessageSendingOperations messagingTemplate,
                        SessionRepository<MapSession> repository) {
        super();
        this.repository = repository;
        this.messagingTemplate = messagingTemplate;
    }

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
                log.info("No user connected event");
                return;
            }

            var session = new MapSession(id);
            session.setAttribute("user", event.getUser());

            repository.save(session);

            messagingTemplate.convertAndSendToUser(
                    event.getUser().getName(),
                    START_TOPIC,
                    "Save session for user: " + event.getUser().getName());

            log.debug("stomp session established");
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

            ofNullable(repository.findById(id)).ifPresent(user -> {
                        repository.deleteById(user.getId());

                        messagingTemplate.convertAndSendToUser(
                                event.getUser().getName(),
                                START_TOPIC,
                                "Clear session for user: " + event.getUser().getName());
                    }
            );

            log.debug("stomp session destroyed");
        }

    }

}
