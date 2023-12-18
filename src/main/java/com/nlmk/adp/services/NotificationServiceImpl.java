package com.nlmk.adp.services;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.nlmk.adp.commons.kafka.KafkaHttpProxyProducer;
import com.nlmk.adp.db.dao.InvalidNotificationsDao;
import com.nlmk.adp.db.dao.NotificationDao;
import com.nlmk.adp.db.dao.NotificationEmailDao;
import com.nlmk.adp.db.repository.NotificationRepository;
import com.nlmk.adp.kafka.dto.NotificationBaseDto;
import com.nlmk.adp.kafka.dto.NotificationDto;
import com.nlmk.adp.services.component.AuthJwt;
import com.nlmk.adp.services.component.PrincipalJwt;
import com.nlmk.adp.services.mapper.DtoToKafkaMessageMapper;
import com.nlmk.adp.services.mapper.NotificationToDtoMapper;
import nlmk.l3.mesadp.DbUserNotificationVer0;

/**
 * NotificationServiceImpl.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    @Value("${spring.kafka.producer.topic.notification-messsage}")
    private String topic;

    private final ObjectMapper objectMapper;

    private final AuthJwt authJwt;

    private final KafkaHttpProxyProducer kafkaHttpProxyProducer;

    private final NotificationDao notificationDao;

    private final NotificationEmailDao notificationEmailDao;

    private final InvalidNotificationsDao invalidNotificationsDao;

    private final NotificationRepository notificationRepository;

    private final SocketMessageSenderService socketMessageSenderService;

    private final DtoToKafkaMessageMapper dtoToKafkaMessageMapper;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final NotificationToDtoMapper notificationToDtoMapper;

    private final PrincipalJwt principalJwt;

    @Override
    public void send(NotificationDto body) {
        var existed = notificationRepository.findById(body.id());
        if (existed.isPresent()) {
            log.info("Handled message is presented, uuid: " + body.id());
            return;
        }

        notificationDao.save(body);
        applicationEventPublisher.publishEvent(body);
    }

    @Override
    public void sendV2(NotificationDto body) {
        socketMessageSenderService.send(body);
    }

    @Override
    public void sendToKafka(NotificationDto body) {
        var snapshot = dtoToKafkaMessageMapper.mapDataFromDto(body);
        var response = kafkaHttpProxyProducer.send(topic, snapshot).block();
        log.info("Sent, result: {}", response);
    }

    @Override
    public void sendTrashToKafka(DbUserNotificationVer0 body) {
        var response = kafkaHttpProxyProducer.send(topic, body).block();
        log.info("Sent, result: {}", response);
    }

    @Override
    public void invalidate(Object body, String reason) {
        var snapshot = objectMapper.valueToTree(body);
        invalidNotificationsDao.save(snapshot, reason);
    }

    @Override
    public NotificationDto getById(UUID id) {
        return notificationDao.getById(id);
    }

    @Override
    public List<NotificationBaseDto> getBacklogNotificationsForCurrentUser(Integer limit) {
        var keyckloakUserDto = authJwt.getKeyckloakUserDto();
        var roles = keyckloakUserDto.getRealmAccess().getRoles();
        return notificationRepository.findBacklogByUserInfo(keyckloakUserDto.getEmail(), roles, limit)
                                     .stream()
                                     .map(notificationToDtoMapper::mapToBaseDto)
                                     .toList();
    }

    @Override
    public void markAllReadedByEmail(Set<UUID> uuids) {
        var email = principalJwt.getName();
        var roles = principalJwt.getRoles();
        notificationEmailDao.markAllReadedByEmail(email, roles, uuids);
    }

}
