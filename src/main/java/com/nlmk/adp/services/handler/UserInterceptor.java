package com.nlmk.adp.services.handler;

import com.nlmk.adp.dto.JwtAuthentication;
import com.nlmk.adp.dto.StompAuthenticationToken;
import com.nlmk.adp.services.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Component;

import java.security.Principal;

import static java.util.Optional.ofNullable;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserInterceptor implements ChannelInterceptor {

    private final AuthService authService;
    @Value("${websocket.topic.start:/topic/hello}")
    private String START_TOPIC;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        switch (ofNullable(accessor)
                .map(acc -> acc.getCommand())
                .orElse(StompCommand.ERROR)) {
            case CONNECT -> {
                final String token = accessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION);

                final StompAuthenticationToken user = (StompAuthenticationToken) ofNullable(token)
                        .map(m -> {
                            var auth = new JwtAuthentication(null);
                            auth.setCredentialsToken(token);
                            return authService.authenticate(auth);
                        })
                        .orElseThrow(() -> new OAuth2AuthenticationException("Access Denied"));

                accessor.setUser(user);
                accessor.setLeaveMutable(true);
                return MessageBuilder.createMessage(message.getPayload(), accessor.getMessageHeaders());
            }
            case SUBSCRIBE -> {
                Principal userPrincipal = accessor.getUser();
                if (!validateSubscription(userPrincipal, accessor.getDestination())) {
                    throw new IllegalArgumentException("No permission for this topic");
                }
            }
            case ERROR -> {
                log.info("stomp command not specified");
            }
        }
        return message;
    }

    private boolean validateSubscription(Principal principal, String topicDestination) {
        if (principal == null) {
            // Unauthenticated user
            return false;
        }
        log.debug("Validate subscription for {} to topic {}", principal.getName(), topicDestination);
        return topicDestination.equalsIgnoreCase(START_TOPIC);
    }

}
