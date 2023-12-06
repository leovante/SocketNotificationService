package com.nlmk.adp.config;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import com.nlmk.adp.services.NotificationService;

/**
 * Конфигурация получения сообщений по кафка.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig extends KafkaProperties.Consumer {

    public static final int RETRY_TIMEOUT = 1000;

    @Bean
    DefaultErrorHandler errorHandler(NotificationService notificationService) {
        return new DefaultErrorHandler(
                (rec, ex) -> handler(notificationService, rec, ex),
                new FixedBackOff(RETRY_TIMEOUT, 2)
        );
    }

    private static void handler(
            NotificationService notificationService,
            ConsumerRecord<?, ?> rec, Exception ex
    ) {
        var cause = ex.getCause();
        if (cause instanceof ConstraintViolationException) {
            notificationService.invalidate(rec.value(), ex.getCause().getMessage());
        } else {
            notificationService.invalidate(
                    rec.value(),
                    String.format("%s : %s", cause.getClass().getName(), cause.getMessage())
            );
        }
    }

}
