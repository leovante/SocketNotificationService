package com.nlmk.adp.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

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
            @Value("${spring.kafka.consumer.topic.notification-messsage}")
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
            ListenableFuture<SendResult<String, DbUserNotificationVer0>> future =
                    messageKafkaTemplate.send(topic, body);
            future.addCallback(new ListenableFutureCallback<SendResult<String, DbUserNotificationVer0>>() {
                @Override
                public void onFailure(Throwable ex) {
                    log.info("Unable to send message - {}, error message - {}", body, ex.getMessage());
                }

                @Override
                public void onSuccess(SendResult<String, DbUserNotificationVer0> result) {
                    log.info("Successfully delivered message to {}. Offset: {}, Partition: {}",
                             topic, result.getRecordMetadata().offset(), result.getRecordMetadata().partition());
                }
            });
        } catch (RuntimeException ex) {
            log.info("Unable to send message - {}, error message - {}", body, ex.getMessage());
            throw new RuntimeException("KAFKA_PRODUCER_ERROR");
        }
    }

}
