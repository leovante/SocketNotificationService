package com.nlmk.adp.services.interceptor_websocket;

import com.nlmk.adp.dto.JwtAuthentication;
import com.nlmk.adp.services.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.adapters.springsecurity.KeycloakAuthenticationException;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.session.MapSession;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserInterceptor extends ChannelInterceptorAdapter {

    private final SessionRepository<MapSession> repository;
    private final AuthService authService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        switch (ofNullable(accessor)
                .map(acc -> acc.getCommand())
                .orElse(StompCommand.ERROR)) {
            case CONNECT -> {
                final String token = accessor.getFirstNativeHeader("Authorization");

                final KeycloakAuthenticationToken user = (KeycloakAuthenticationToken) ofNullable(token)
                        .map(m -> {
                            var auth = new JwtAuthentication(null);
                            auth.setCredentialsToken(token);
                            return authService.authenticate(auth);
                        })
                        .orElseThrow(() -> new KeycloakAuthenticationException("Access Denied"));

                accessor.setUser(user);
                accessor.setLeaveMutable(true);
                return MessageBuilder.createMessage(message.getPayload(), accessor.getMessageHeaders());
            }
            case DISCONNECT -> {
                MessageHeaders headers = message.getHeaders();
                var attr = Optional.of((Map<String, String>) headers.get("simpSessionAttributes"));
                var id = attr.map(i -> i.get("SPRING.SESSION.ID")).orElse("");

                ofNullable(repository.findById(id)).ifPresent(user ->
                        repository.deleteById(user.getId())
                );

                log.debug("websocket session destroyed");
            }
            case ERROR -> {
                log.info("stomp command not specified");
            }
        }
        return message;
    }
}
