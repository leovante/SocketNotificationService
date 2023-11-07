package com.nlmk.adp.services.handler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.session.MapSession;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import static java.util.Optional.ofNullable;

@Slf4j
@Component
public class StompHandler {

    private final SessionRepository repository;
    private final SimpMessageSendingOperations messagingTemplate;


    public StompHandler(SimpMessageSendingOperations messagingTemplate,
                        SessionRepository repository) {
        super();
        this.repository = repository;
        this.messagingTemplate = messagingTemplate;
    }

    @Component
    class ConnectEvent<S> implements ApplicationListener<SessionConnectEvent> {

        @Override
        public void onApplicationEvent(SessionConnectEvent event) {
            var headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
            var nativeHeaders = headers.getNativeHeader("nativeHeader");

            String id = SimpMessageHeaderAccessor.getSessionId(headers.getMessageHeaders());

            var session = new MapSession(id);
            repository.save(session);
            log.debug("stomp session established");
        }

    }


    @Component
    class DisconnectEvent<S> implements ApplicationListener<SessionDisconnectEvent> {

        @Override
        public void onApplicationEvent(SessionDisconnectEvent event) {
            var headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
            var session = SimpMessageHeaderAccessor.getSessionId(headers.getMessageHeaders());

            ofNullable(repository.findById(session)).ifPresent(user ->
                    repository.deleteById(user.getId())
            );
            log.debug("stomp session destroyed");
        }

    }

}
