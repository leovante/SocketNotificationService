package com.nlmk.adp.services.handler;

import java.security.Principal;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.stereotype.Component;

import com.nlmk.adp.dto.JwtAuthentication;

/**
 * UserInterceptor.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class UserInterceptor implements ChannelInterceptor {

    private final AuthenticationManager authService;

    @Value("${websocket.topic.start:/user/topic/notification}")
    private String startTopic;

    @Value("${websocket.topic.log:/topic/log}")
    private String logTopic;

    /**
     * preSend.
     *
     * @param message
     *         message
     * @param channel
     *         channel
     *
     * @return Message
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        switch (Optional.ofNullable(accessor)
                        .map(acc -> acc.getCommand())
                        .orElse(StompCommand.ERROR)) {
            case CONNECT -> {
                final String token = accessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION);

                final JwtAuthentication user = Optional
                        .ofNullable(token)
                        .map(m -> {
                            var auth = new BearerTokenAuthenticationToken(token);
                            return authService.authenticate(auth);
                        })
                        .map(it -> (JwtAuthentication) it)
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
            default -> log.info("stomp command not specified");
        }
        return message;
    }

    /**
     * validateSubscription.
     *
     * @param principal
     *         principal
     * @param topicDestination
     *         topicDestination
     *
     * @return boolean
     */
    private boolean validateSubscription(Principal principal, String topicDestination) {
        if (principal == null) {
            // Unauthenticated user
            return false;
        }
        log.debug("Validate subscription for {} to topic {}", principal.getName(), topicDestination);
        return topicDestination.equalsIgnoreCase(startTopic);
    }

}
