package com.nlmk.adp.config;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.boot.autoconfigure.session.SessionProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.MapSession;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;

/**
 * Конфигурация спринг сессии.
 */
@Configuration
@EnableSpringHttpSession
public class SpringHttpSessionConfig {

    /**
     * репозиторий сессиий.
     *
     * @return SessionRepository
     */
    @Bean
    public SessionRepository<MapSession> sessionRepository(
            SessionProperties sessionProperties
    ) {
        var sessionRepository = new MapSessionRepository(new ConcurrentHashMap<>());
        sessionRepository.setDefaultMaxInactiveInterval(sessionProperties.getTimeout());
        return sessionRepository;
    }

}
