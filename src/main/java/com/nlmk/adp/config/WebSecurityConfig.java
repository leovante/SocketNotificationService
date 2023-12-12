package com.nlmk.adp.config;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Конфигурация для PreAuthorize.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    /**
     * 123.
     *
     * @param http
     *         123.
     *
     * @return 123.
     *
     * @throws Exception
     *         123.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(this::jwtConfigurer)
                .sessionManagement(sessionManagementConfig -> {
                    sessionManagementConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                    sessionManagementConfig.sessionFixation(
                            SessionManagementConfigurer.SessionFixationConfigurer::none
                    );
                })
                .authorizeHttpRequests(auth -> auth.requestMatchers("/").permitAll()
                                                   .requestMatchers("/api/*").permitAll()
                                                   .requestMatchers("/error").permitAll()
                                                   .requestMatchers("/swagger-ui/*").permitAll()
                                                   .requestMatchers("/v3/api-docs/**").permitAll()
                                                   .requestMatchers("/actuator/**").permitAll()
                                                   .requestMatchers("/ws/**").permitAll()
                                                   .anyRequest().authenticated())
                .build();
    }

    /**
     * Создаёт converter для получения ролей пользователя из jwt токена от keycloak.
     */
    private OAuth2ResourceServerConfigurer<HttpSecurity> jwtConfigurer(
            OAuth2ResourceServerConfigurer<HttpSecurity> oauth2
    ) {
        Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter = jwt -> {
            Map<String, Collection<String>> realmAccess = jwt.getClaim("realm_access");
            Collection<String> roles = realmAccess.get("roles");
            return roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toSet());
        };

        var jwtAuthConverter = new JwtAuthenticationConverter();
        jwtAuthConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        jwtAuthConverter.setPrincipalClaimName("email");

        return oauth2.jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(jwtAuthConverter));
    }

}
