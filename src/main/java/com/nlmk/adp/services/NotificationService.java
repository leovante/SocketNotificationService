package com.nlmk.adp.services;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.nlmk.adp.db.dao.NotificationDao;
import com.nlmk.adp.db.dao.NotificationEmailDao;
import com.nlmk.adp.kafka.dto.NotificationBaseDto;
import com.nlmk.adp.kafka.dto.NotificationDto;
import com.nlmk.adp.services.component.PrincipalJwt;
import com.nlmk.adp.services.mapper.NotificationToDtoMapper;

/**
 * NotificationServiceImpl.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final PrincipalJwt principalJwt;

    private final NotificationDao notificationDao;

    private final NotificationEmailDao notificationEmailDao;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final NotificationToDtoMapper notificationToDtoMapper;

    /**
     * send.
     *
     * @param body
     *         body
     */
    public void send(NotificationDto body) {
        var existed = notificationDao.getById(body.id());
        if (existed.isPresent()) {
            log.info("Handled message is presented, uuid: " + body.id());
            return;
        }

        notificationDao.saveNew(body);
        applicationEventPublisher.publishEvent(body);
    }

    /**
     * ByRole.
     *
     * @param limit
     *         limit.
     *
     * @return NotificationBaseDto.
     */
    public List<NotificationBaseDto> getBacklogNotificationsForCurrentUser(Integer limit) {
        var roles = principalJwt.getRoles();
        var email = principalJwt.getName();
        return notificationDao.getBacklog(email, roles, limit)
                              .stream()
                              .map(notificationToDtoMapper::mapToBaseDto)
                              .toList();
    }

    /**
     * Пометить уведомления прочитанными.
     *
     * @param uuids
     *         идентификаторы уведомлений.
     */
    public void markNotificationsAsReadByUser(Set<UUID> uuids) {
        var email = principalJwt.getName();
        var roles = principalJwt.getRoles();
        notificationEmailDao.markNotificationsAsReadByUser(email, roles, uuids);
    }

}
