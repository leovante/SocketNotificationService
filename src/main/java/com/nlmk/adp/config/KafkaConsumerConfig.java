package com.nlmk.adp.config;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.DefaultErrorHandler;

import com.nlmk.adp.services.NotificationService;

/**
 * Конфигурация получения собщений по кафка.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig extends KafkaProperties.Consumer {

    private final KafkaProperties common;

//    /**
//     * messageConsumerFactory.
//     *
//     * @return ConsumerFactory
//     */
//    @Qualifier("messageConsumerFactory")
//    @Bean
//    public ConsumerFactory<byte[], DbUserNotificationVer0> messageConsumerFactory() {
//        var conf = new HashMap<>(this.common.buildConsumerProperties());
//        conf.putAll(this.buildProperties());
//        var factory = new DefaultKafkaConsumerFactory<byte[], DbUserNotificationVer0>(conf);
//        var deserializer = new ErrorHandlingDeserializer<DbUserNotificationVer0>(
//                new KafkaAvroDeserializer()
//        );
//        factory.setValueDeserializer(deserializer);
//
//        return factory;
//    }

//    /**
//     * kafkaListenerContainerFactory.
//     *
//     * @param messageConsumerFactory
//     *         messageConsumerFactory
//     *
//     * @return ConcurrentKafkaListenerContainerFactory
//     */
//    @SuppressWarnings("UnnecessaryParentheses")
//    @Bean(name = "messageConsumerContainerFactory")
//    public ConcurrentKafkaListenerContainerFactory<String, DbUserNotificationVer0> kafkaListenerContainerFactory(
//            ConsumerFactory<String, DbUserNotificationVer0> messageConsumerFactory,
//            NotificationService notificationService) {
//
//        final ConcurrentKafkaListenerContainerFactory<String, DbUserNotificationVer0> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(messageConsumerFactory);
//        factory.setCommonErrorHandler(errorHandler(notificationService));
//        return factory;
//    }

    @Bean
    DefaultErrorHandler errorHandler(NotificationService notificationService) {
        return new DefaultErrorHandler((rec, ex) -> {
            var cause = ex.getCause();
            if (cause instanceof ConstraintViolationException) {
                notificationService.invalidate(rec.value(), ex.getCause().getMessage());
            } else {
                notificationService.invalidate(
                        rec.value(),
                        String.format("%s : %s", cause.getClass().getName(), cause.getMessage())
                );
            }
        });
    }

}
