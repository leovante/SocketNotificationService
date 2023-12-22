package com.nlmk.adp.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import com.nlmk.adp.db.dao.InvalidNotificationsDao;

/**
 * Конфигурация получения сообщений по кафка.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig extends KafkaProperties.Consumer {

    @Bean
    DefaultErrorHandler errorHandler(
            InvalidNotificationsDao invalidNotificationsDao,
            KafkaProperties kafkaProperties
    ) {
        var attempts = kafkaProperties.getRetry().getTopic().getAttempts();
        var delay = kafkaProperties.getRetry().getTopic().getDelay().getSeconds() * 1000;
        return new DefaultErrorHandler(
                (rec, ex) -> handler(invalidNotificationsDao, rec, ex),
                new FixedBackOff(delay, attempts)
        );
    }

    private static void handler(
            InvalidNotificationsDao invalidNotificationsDao,
            ConsumerRecord<?, ?> rec, Exception ex
    ) {
        var cause = ex.getCause();
        var recValue = rec.value();
        log.error("Kafka consuming error", ex);
        String message = String.format("%s : %s", cause.getClass().getName(), cause.getMessage());
        try {
            invalidNotificationsDao.saveNew(recValue, message);
        } catch (Exception exp) {
            log.error("Error logging kafka consuming error", exp);
        }
    }

}
