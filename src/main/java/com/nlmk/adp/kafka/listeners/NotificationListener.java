package com.nlmk.adp.kafka.listeners;

import com.nlmk.adp.config.ObjectMapperHelper;
import com.nlmk.adp.dto.NotificationCheck;
import com.nlmk.adp.kafka.dispatcher.NotificationsDispatcher;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nlmk.l3.mesadp.DbUserNotificationVer0;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

import static org.springframework.kafka.support.KafkaHeaders.OFFSET;
import static org.springframework.kafka.support.KafkaHeaders.RECEIVED_PARTITION_ID;
import static org.springframework.kafka.support.KafkaHeaders.RECEIVED_TIMESTAMP;
import static org.springframework.kafka.support.KafkaHeaders.RECEIVED_TOPIC;
import static org.springframework.kafka.support.KafkaHeaders.TIMESTAMP_TYPE;

/**
 * листенер сообщений по кафка.
 */
@Slf4j
@Service
@AllArgsConstructor
@ConditionalOnProperty(value = "spring.kafka.enabled", havingValue = "true")
@Validated
public class NotificationListener {

    private final NotificationsDispatcher notificationsService;

    /**
     * handleNotificationMessage.
     *
     * @param message       message
     * @param topic         topic
     * @param partitionId   partitionId
     * @param offset        offset
     * @param timestamp     timestamp
     * @param timestampType timestampType
     */
    @KafkaListener(
            topics = "${spring.kafka.consumer.topic.notification-messsage}",
            groupId = "${spring.kafka.consumer.group-id}",
            autoStartup = "true",
            containerFactory = "messageConsumerContainerFactory")
    public void handleNotificationMessage(@Valid @NotificationCheck @Payload DbUserNotificationVer0 message,
                                          @Header(RECEIVED_TOPIC) String topic,
                                          @Header(RECEIVED_PARTITION_ID) String partitionId,
                                          @Header(OFFSET) String offset,
                                          @Header(RECEIVED_TIMESTAMP) Long timestamp,
                                          @Header(TIMESTAMP_TYPE) String timestampType) {
        log.info("Receive notification message {}. Partition: {}. Offset: {}. Message: {}",
                topic, partitionId, offset, ObjectMapperHelper.writeValueAsString(message));

        notificationsService.dispatch(message);

    }

}
