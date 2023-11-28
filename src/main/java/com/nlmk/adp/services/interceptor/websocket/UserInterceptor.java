package com.nlmk.adp.services.interceptor.websocket;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.session.MapSession;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;

/**
 * UserInterceptor.
 */
@Slf4j
@Component
public class UserInterceptor implements ChannelInterceptor {

    @Autowired
    private SessionRepository<MapSession> repository;

    @Override
    public Message<?> preSend(
            Message<?> message,
            MessageChannel channel
    ) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        switch (Optional.ofNullable(accessor)
                        .map(acc -> acc.getCommand())
                        .orElse(StompCommand.ERROR)) {
            case CONNECT -> {
                var authToken = SecurityContextHolder.getContext().getAuthentication();
                var principal = (Principal) authToken.getPrincipal();

                accessor.setUser(principal);
            }
            case DISCONNECT -> {
                MessageHeaders headers = message.getHeaders();
                SimpMessageType type = (SimpMessageType) headers.get("simpMessageType");
                String simpSessionId = (String) headers.get("simpSessionId");
                var v = (Map<String, String>) headers.get("simpSessionAttributes");
                repository.deleteById(v.get("SPRING.SESSION.ID"));
                log.debug("websocket session destroyed");
            }
            case ERROR -> {
                log.info("stomp command not specified");
            }
            default -> {
                log.info("default not specified");
            }
        }
        return message;
    }

}
