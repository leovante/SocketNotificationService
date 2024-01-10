package com.nlmk.adp.db.dao;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nlmk.adp.db.entity.NotificationEntity;
import com.nlmk.adp.db.repository.NotificationRepository;
import com.nlmk.adp.kafka.dto.NotificationDto;
import com.nlmk.adp.services.mapper.NotificationToDaoMapper;

/**
 * Для работы с валидными уведомлениями.
 */
@Service
@Transactional
@AllArgsConstructor
public class NotificationDao {

    private final NotificationToDaoMapper notificationDaoMapper;

    private final NotificationRepository notificationRepository;

    /**
     * save.
     *
     * @param dto
     *         dto
     *
     * @return NotificationEntity
     */
    public NotificationEntity saveNew(NotificationDto dto) {
        var entity = notificationDaoMapper.mapDtoToEntity(dto);

        var roles = entity.getNotificationRolesEntities();
        var emails = entity.getNotificationUserSuccessEntities();
        roles.forEach(i -> i.setNotification(entity));
        emails.forEach(i -> i.setNotification(entity));

        return notificationRepository.save(entity);
    }

    /**
     * by id.
     *
     * @param id
     *         id.
     *
     * @return dto.
     */
    public Optional<NotificationEntity> getById(UUID id) {
        return notificationRepository.findById(id);
    }

    /**
     * Бэклог-сообщения.
     *
     * @param userEmail
     *         email юзера.
     * @param userRoles
     *         роли юзера.
     * @param limit
     *         лимит сообщений бэклога.
     *
     * @return уведомления.
     */
    public List<NotificationEntity> getBacklog(
            String userEmail,
            Set<String> userRoles,
            Integer limit
    ) {
        return notificationRepository.findBacklogByUserInfo(userEmail, userRoles, limit);
    }

}
