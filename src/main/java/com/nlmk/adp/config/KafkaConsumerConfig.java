package com.nlmk.adp.config;

import java.util.HashMap;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.nlmk.adp.services.NotificationService;
import nlmk.l3.mesadp.DbUserNotificationVer0;

/**
 * Конфигурация получения собщений по кафка.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig extends KafkaProperties.Consumer {

    private final KafkaProperties common;

    /**
     * messageConsumerFactory.
     *
     * @return ConsumerFactory
     */
    @Qualifier("messageConsumerFactory")
    @Bean
    public ConsumerFactory<String, DbUserNotificationVer0> messageConsumerFactory() {
        var conf = new HashMap<>(this.common.buildConsumerProperties());
        conf.putAll(this.buildProperties());
        var factory = new DefaultKafkaConsumerFactory<String, DbUserNotificationVer0>(conf);
        var deserializer = new ErrorHandlingDeserializer<>(
                new JsonDeserializer<>(DbUserNotificationVer0.class, false)
        );
        factory.setValueDeserializer(deserializer);

        return factory;
    }

    /**
     * kafkaListenerContainerFactory.
     *
     * @param messageConsumerFactory
     *         messageConsumerFactory
     *
     * @return ConcurrentKafkaListenerContainerFactory
     */
    @SuppressWarnings("UnnecessaryParentheses")
    @Bean(name = "messageConsumerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, DbUserNotificationVer0> kafkaListenerContainerFactory(
            ConsumerFactory<String, DbUserNotificationVer0> messageConsumerFactory,
            NotificationService notificationService) {

        final ConcurrentKafkaListenerContainerFactory<String, DbUserNotificationVer0> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(messageConsumerFactory);
        factory.setCommonErrorHandler(errorHandler(notificationService));
        return factory;
    }

    @Bean
    DefaultErrorHandler errorHandler(NotificationService notificationService) {
        return new DefaultErrorHandler((rec, ex) -> {
            var payload = (ConsumerRecord<String, DbUserNotificationVer0>) rec;
            var cause = ex.getCause();
            if (cause instanceof ConstraintViolationException) {
                notificationService.invalidate(payload.value(), ex.getCause().getMessage());
            }
        });
    }

}
