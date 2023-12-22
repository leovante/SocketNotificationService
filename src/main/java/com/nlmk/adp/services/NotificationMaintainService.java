package com.nlmk.adp.services;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nlmk.adp.commons.kafka.KafkaHttpProxyProducer;
import com.nlmk.adp.db.dao.NotificationDao;
import com.nlmk.adp.kafka.dto.NotificationDto;
import com.nlmk.adp.services.mapper.DtoToKafkaMessageMapper;
import com.nlmk.adp.services.mapper.NotificationToDtoMapper;
import nlmk.l3.mesadp.DbUserNotificationVer0;

/**
 * Служебный функционал.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationMaintainService {

    @Value("${spring.kafka.producer.topic.notification-messsage}")
    private String topic;

    private final KafkaHttpProxyProducer kafkaHttpProxyProducer;

    private final NotificationDao notificationDao;

    private final SocketMessageSenderService socketMessageSenderService;

    private final DtoToKafkaMessageMapper dtoToKafkaMessageMapper;

    private final NotificationToDtoMapper notificationToDtoMapper;

    /**
     * Широковещательная отправка мимо кафки.
     *
     * @param body
     *         body
     */
    public void sendWithoutKafka(NotificationDto body) {
        socketMessageSenderService.send(body);
    }

    /**
     * sendToKafka.
     *
     * @param body
     *         body
     */
    public void sendToKafka(NotificationDto body) {
        var snapshot = dtoToKafkaMessageMapper.mapDataFromDto(body);
        var response = kafkaHttpProxyProducer.send(topic, snapshot).block();
        log.info("Sent, result: {}", response);
    }

    /**
     * Отправить в кафку авро-сообщение с произвольными данными для тестирования валидации.
     *
     * @param message
     *         сообщение.
     */
    public void sendAnythingToKafka(DbUserNotificationVer0 message) {
        var response = kafkaHttpProxyProducer.send(topic, message).block();
        log.info("Sent anything, result: {}", response);
    }

    /**
     * ById.
     *
     * @param id
     *         id.
     *
     * @return notif.
     */
    public NotificationDto getById(UUID id) {
        return notificationDao.getById(id).map(notificationToDtoMapper::mapToDto).orElseThrow();
    }

}
