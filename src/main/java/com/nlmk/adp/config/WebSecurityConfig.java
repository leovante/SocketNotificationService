package com.nlmk.adp.config;

import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * Конфигурация для PreAuthorize.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
@ConditionalOnProperty(value = "keycloak.enabled", havingValue = "true")
public class WebSecurityConfig /*extends AbstractSecurityWebSocketMessageBrokerConfigurer*/ {

    // spring boot ver.4 or above
    /*protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .simpDestMatchers("/topic/**").denyAll()
                .simpDestMatchers("/**").hasRole("ADMIN");
    }*/

}
