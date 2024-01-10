package com.nlmk.adp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Конфигурация для PreAuthorize.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    public static final String SUPER_USER_ROLE = "super-user";

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
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            AuthenticationManager authenticationManager
    ) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(it -> it.authenticationManagerResolver(context -> authenticationManager))
                .sessionManagement(sessionManagementConfig -> {
                    sessionManagementConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                    sessionManagementConfig.sessionFixation(
                            SessionManagementConfigurer.SessionFixationConfigurer::none
                    );
                })
                .authorizeHttpRequests(auth -> auth.requestMatchers("/").permitAll()
                                                   .requestMatchers("/api/*").permitAll()
                                                   .requestMatchers("/api/maintain/*").hasAuthority(SUPER_USER_ROLE)
                                                   .requestMatchers("/error").permitAll()
                                                   .requestMatchers("/swagger-ui/*").permitAll()
                                                   .requestMatchers("/v3/api-docs/**").permitAll()
                                                   .requestMatchers("/actuator/**").permitAll()
                                                   .requestMatchers("/ws/**").permitAll()
                                                   .anyRequest().authenticated())
                .build();
    }

}
