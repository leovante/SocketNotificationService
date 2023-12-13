package com.nlmk.adp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;

/**
 * Конфиг безопасности для веб сокетов.
 */
@Configuration
@EnableWebSocketSecurity
public class WebSocketSecurityConfig {

    /**
     * Auth manager.
     *
     * @param messages
     *         messages.
     *
     * @return AuthorizationManager.
     */
    @Bean
    AuthorizationManager<Message<?>> messageAuthorizationManager(
            MessageMatcherDelegatingAuthorizationManager.Builder messages
    ) {
        messages
                .nullDestMatcher().authenticated()
                .simpDestMatchers("/ws/**").authenticated()
                .simpSubscribeDestMatchers("/topic/**").hasRole("super-user")
                .simpSubscribeDestMatchers("/user/**").hasRole("super-user")
                .anyMessage().denyAll();
        return messages.build();
    }

}
