package com.nlmk.adp.kafka.listeners;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.nlmk.adp.config.ObjectMapperHelper;
import com.nlmk.adp.kafka.dispatcher.KafkaListenerDispatcher;
import com.nlmk.adp.services.mapper.KafkaMessageToDtoMapper;
import nlmk.l3.mesadp.DbUserNotificationVer0;

/**
 * Листенер сообщений по кафка.
 */
@Slf4j
@Service
@AllArgsConstructor
public class NotificationListener {

    private final KafkaListenerDispatcher dispatcher;

    private final KafkaMessageToDtoMapper messageToDtoMapper;

    /**
     * Слушает новые уведомления.
     *
     * @param message
     *         сообщение.
     */
    @KafkaListener(
            topics = "${spring.kafka.consumer.topic.notification-messsage}"
    )
    public void handleNotificationMessage(
            @Payload DbUserNotificationVer0 message
    ) {
        log.info("Receive notification message {}", ObjectMapperHelper.writeValueAsString(message));
        var dto = messageToDtoMapper.mapDataToDto(message);
        var operation = message.getOp();
        dispatcher.dispatch(operation, dto);
    }

}
