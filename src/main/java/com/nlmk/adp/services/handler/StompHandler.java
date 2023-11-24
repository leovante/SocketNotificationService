package com.nlmk.adp.services.handler;


import lombok.extern.slf4j.Slf4j;
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


    public StompHandler(SimpMessageSendingOperations messagingTemplate,
                        SessionRepository<MapSession> repository) {
        super();
        this.repository = repository;
        this.messagingTemplate = messagingTemplate;
    }

    @Component
    class ConnectEvent<S> implements ApplicationListener<SessionConnectEvent> {

        @Override
        public void onApplicationEvent(SessionConnectEvent event) {
            String id = Optional.ofNullable(event.getUser())
                    .map(Principal::getName)
                    .orElse(null);

            if (id == null) {
                return;
            }

            var session = new MapSession(id);
            session.setAttribute("user", event.getUser());
            repository.save(session);

            log.debug("stomp session established");
        }

    }

    @Component
    class DisconnectEvent<S> implements ApplicationListener<SessionDisconnectEvent> {

        @Override
        public void onApplicationEvent(SessionDisconnectEvent event) {
            String id = Optional.ofNullable(event.getUser())
                    .map(Principal::getName)
                    .orElse("");

            ofNullable(repository.findById(id)).ifPresent(user ->
                    repository.deleteById(user.getId())
            );
            log.debug("stomp session destroyed");
        }

    }

}
