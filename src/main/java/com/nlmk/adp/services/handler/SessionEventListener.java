package com.nlmk.adp.services.handler;

import java.util.Optional;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

/**
 * SessionEventListener.
 */
public class SessionEventListener {

    private final ParticipantRepository participantRepository;

    /**
     * SessionEventListener.
     *
     * @param participantRepository participantRepository
     */
    public SessionEventListener(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    /**
     * handleSessionConnect.
     *
     * @param event event
     */
    @EventListener
    public void handleSessionConnect(SessionConnectEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String username = headers.getUser().getName();

        // We store the session as we need to be idempotent in the disconnect event processing
        participantRepository.add(headers.getSessionId(), username);
    }

    /**
     * handleSessionConnected.
     *
     * @param event event
     */
    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String username = headers.getUser().getName();

        // We store the session as we need to be idempotent in the disconnect event processing
        participantRepository.add(headers.getSessionId(), username);
    }

    /**
     * handleSessionDisconnect.
     *
     * @param event event
     */
    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        Optional.ofNullable(participantRepository.getParticipant(event.getSessionId()))
                .ifPresent(login ->
                        participantRepository.removeParticipant(event.getSessionId())
                );
    }

    /**
     * handleSessionDisconnect.
     *
     * @param event event
     */
    @EventListener
    public void handleSessionDisconnect(SessionSubscribeEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String username = headers.getUser().getName();

        // We store the session as we need to be idempotent in the disconnect event processing
        participantRepository.add(headers.getSessionId(), username);
    }

}
