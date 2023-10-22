package com.nlmk.adp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Конфигурация jpa-аудита.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
