package com.nlmk.adp.kafka.listeners;

import javax.validation.Valid;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.nlmk.adp.config.ObjectMapperHelper;
import com.nlmk.adp.dto.NotificationCheck;
import com.nlmk.adp.kafka.dispatcher.NotificationsDispatcher;
import nlmk.l3.mesadp.DbUserNotificationVer0;

/**
 * листенер сообщений по кафка.
 */
@Slf4j
@Service
@AllArgsConstructor
@Validated
public class NotificationListener {

    private final NotificationsDispatcher notificationsService;

    /**
     * handleNotificationMessage.
     *
     * @param message
     *         message
     * @param topic
     *         topic
     * @param partitionId
     *         partitionId
     * @param offset
     *         offset
     * @param timestamp
     *         timestamp
     * @param timestampType
     *         timestampType
     */
    @KafkaListener(
            topics = "${spring.kafka.consumer.topic.notification-messsage}",
            groupId = "${spring.kafka.consumer.group-id}",
            autoStartup = "true",
            containerFactory = "messageConsumerContainerFactory")
    public void handleNotificationMessage(@Valid @NotificationCheck @Payload DbUserNotificationVer0 message,
                                          @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                          @Header(KafkaHeaders.RECEIVED_PARTITION_ID) String partitionId,
                                          @Header(KafkaHeaders.OFFSET) String offset,
                                          @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp,
                                          @Header(KafkaHeaders.TIMESTAMP_TYPE) String timestampType) {
        log.info("Receive notification message {}. Partition: {}. Offset: {}. Message: {}",
                 topic, partitionId, offset, ObjectMapperHelper.writeValueAsString(message));
        notificationsService.dispatch(message);
    }

}
