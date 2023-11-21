package com.nlmk.adp.config;

import java.util.HashMap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import nlmk.l3.mesadp.DbUserNotificationVer0;

/**
 * Конфигурация kafka producer.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "spring.kafka.producer")
@ConditionalOnProperty(value = "spring.kafka.enabled", havingValue = "true")
public class KafkaProducerConfig extends KafkaProperties.Producer {

    private final KafkaProperties common;

    /**
     * messageProducerFactory.
     *
     * @return ProducerFactory
     */
    @Qualifier("messageProducerFactory")
    @Bean
    public ProducerFactory<String, DbUserNotificationVer0> messageProducerFactory() {
        final var conf = new HashMap<>(
                this.common.buildProducerProperties()
        );
        conf.putAll(this.buildProperties());
        DefaultKafkaProducerFactory<String, DbUserNotificationVer0> producerFactory =
                new DefaultKafkaProducerFactory<>(conf);

        return producerFactory;
    }

    /**
     * messageKafkaTemplate.
     *
     * @param producerFactory
     *         producerFactory
     *
     * @return KafkaTemplate
     */
    @Qualifier("messageKafkaProducerTemplate")
    @Bean
    public KafkaTemplate<String, DbUserNotificationVer0> messageKafkaTemplate(
            @Qualifier("messageProducerFactory") ProducerFactory<String, DbUserNotificationVer0> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

}
