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
@EnableWebSecurity
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

    /**
     * 123.
     *
     * @param http
     *         123.
     *
     * @return 123.
     *
     * @throws Exception 123.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.cors(Customizer.withDefaults())
                   //                   .oauth2ResourceServer(
                   //                           oAuth2ResourceServerConfig ->
                   //                                   oAuth2ResourceServerConfig.jwt(
                   //                                           jwtConfigurer ->
                   //                                                   jwtConfigurer.jwtAuthenticationConverter(
                   //                                                           jwtAuthenticationConverterForKeycloak()
                   //                                                   )
                   //                                   )
                   //                   )
                   .sessionManagement(sessionManagementConfig -> {
                       sessionManagementConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                       sessionManagementConfig.sessionFixation(
                               SessionManagementConfigurer.SessionFixationConfigurer::none
                       );
                   })
                   .authorizeHttpRequests(auth -> auth.requestMatchers("/").permitAll()
                                                      .requestMatchers("/error").permitAll()
                                                      .requestMatchers("/swagger-ui/*").permitAll()
                                                      .requestMatchers("/v3/api-docs/**").permitAll()
                                                      .requestMatchers("/actuator/**").permitAll()
                                                      .anyRequest().authenticated())
                   .build();
    }

}
