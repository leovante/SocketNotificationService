package com.nlmk.adp.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nlmk.adp.commons.kafka.KafkaHttpProxyProducer;
import com.nlmk.adp.config.ObjectMapperHelper;
import com.nlmk.adp.db.dao.InvalidNotificationsDaoService;
import com.nlmk.adp.db.dao.NotificationDaoService;
import com.nlmk.adp.db.repository.NotificationRepository;
import com.nlmk.adp.kafka.dto.NotificationDto;
import com.nlmk.adp.services.mapper.NotificationFromDtoMapper;

/**
 * NotificationServiceImpl.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    @Value("${spring.kafka.producer.topic.notification-messsage}")
    private String topic;

    private final KafkaHttpProxyProducer kafkaHttpProxyProducer;

    private final NotificationDaoService notificationDaoService;

    private final NotificationRepository notificationRepository;

    private final InvalidNotificationsDaoService invalidNotificationsDaoService;

    private final SocketMessageSenderService socketMessageSenderService;

    private final NotificationFromDtoMapper notificationFromDtoMapper;

    @Override
    public void send(NotificationDto body) {
        var existed = notificationRepository.findById(body.uuid());
        if (existed.isPresent()) {
            log.info("Handled message is presented, uuid: " + body.uuid());
            return;
        }

        notificationDaoService.save(body);
        socketMessageSenderService.send(body.href());
    }

    @Override
    public void sendV2(NotificationDto body) {
        socketMessageSenderService.send(body.href());
    }

    @Override
    public void sendToKafka(NotificationDto body) {
        var snapshot = notificationFromDtoMapper.mapDataFromDto(body);
        var response = kafkaHttpProxyProducer.send(topic, snapshot).block();
        log.info("Sent, result: {}", response);
    }

    @Override
    public void invalidate(Object body, String reason) {
        var snapshot = ObjectMapperHelper.getObjectMapper().valueToTree(body);
        invalidNotificationsDaoService.save(snapshot, reason);
    }

}
