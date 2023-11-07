package com.nlmk.adp.services.handler;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.Optional;

//@Component
public class SessionEventListener {

    private final ParticipantRepository participantRepository;


    public SessionEventListener(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    @EventListener
    public void handleSessionConnect(SessionConnectEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String username = headers.getUser().getName();

        // We store the session as we need to be idempotent in the disconnect event processing
        participantRepository.add(headers.getSessionId(), username);
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        Optional.ofNullable(participantRepository.getParticipant(event.getSessionId()))
                .ifPresent(login ->
                        participantRepository.removeParticipant(event.getSessionId())
                );
    }

    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String username = headers.getUser().getName();

        // We store the session as we need to be idempotent in the disconnect event processing
        participantRepository.add(headers.getSessionId(), username);
    }

    @EventListener
    public void handleSessionDisconnect(SessionSubscribeEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String username = headers.getUser().getName();

        // We store the session as we need to be idempotent in the disconnect event processing
        participantRepository.add(headers.getSessionId(), username);
    }


}
