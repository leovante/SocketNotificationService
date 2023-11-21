package com.nlmk.adp.config;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${spring.websocket.session.timeout:300}")
    private Integer maxInactiveIntervalInSeconds;

    /**
     * репозиторий сессиий.
     *
     * @return SessionRepository
     */
    @Bean
    public SessionRepository<MapSession> sessionRepository() {
        var sessionRepository =  new MapSessionRepository(new ConcurrentHashMap<>());
        ((MapSessionRepository) sessionRepository)
                .setDefaultMaxInactiveInterval(maxInactiveIntervalInSeconds);
        return sessionRepository;
    }

}
