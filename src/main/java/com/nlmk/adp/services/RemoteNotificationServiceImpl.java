package com.nlmk.adp.services;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import nlmk.l3.mesadp.DbUserNotificationVer0;

/**
 * RemoteNotificationServiceImpl.
 */
@Slf4j
@Service
public class RemoteNotificationServiceImpl implements RemoteNotificationService {

    private final String topic;

    private final KafkaTemplate<String, DbUserNotificationVer0> messageKafkaTemplate;

    /**
     * RemoteNotificationServiceImpl.
     *
     * @param messageKafkaTemplate
     *         messageKafkaTemplate
     * @param topic
     *         topic
     */
    public RemoteNotificationServiceImpl(
            @Qualifier("messageKafkaProducerTemplate")
            KafkaTemplate<String, DbUserNotificationVer0> messageKafkaTemplate,
            @Value("${spring.kafka.producer.topic.notification-messsage}")
            String topic) {
        this.messageKafkaTemplate = messageKafkaTemplate;
        this.topic = topic;
    }

    /**
     * send.
     *
     * @param body
     *         body
     */
    @Override
    public void send(DbUserNotificationVer0 body) {
        try {
            CompletableFuture<SendResult<String, DbUserNotificationVer0>> future =
                    messageKafkaTemplate.send(topic, body);

            future.get(5, TimeUnit.SECONDS);

        } catch (Exception ex) {
            log.info("Unable to send message - {}, error message - {}", body, ex.getMessage());
            throw new RuntimeException("KAFKA_PRODUCER_ERROR");
        }
    }

}
