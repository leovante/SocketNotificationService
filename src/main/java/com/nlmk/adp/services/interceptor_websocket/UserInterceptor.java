package com.nlmk.adp.services.interceptor_websocket;

import com.nlmk.adp.services.SecurityService;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.session.MapSession;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;

import java.util.Map;

import static java.util.Optional.ofNullable;

@Slf4j
@Component
public class UserInterceptor extends ChannelInterceptorAdapter {

    @Autowired
    private SessionRepository<MapSession> repository;
    @Autowired
    private SecurityService authService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        switch (ofNullable(accessor)
                .map(acc -> acc.getCommand())
                .orElse(StompCommand.ERROR)) {
            case CONNECT -> {
                final String token = accessor.getFirstNativeHeader("PASSWORD_HEADER");//получить токен
                //добавить валидацию токена
                final KeycloakAuthenticationToken user = authService.getAuthenticatedOrFail(token);

                accessor.setUser(user);
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
        }
        return message;
    }
}
