package com.nlmk.adp.db.dao;

import java.util.UUID;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nlmk.adp.db.entity.NotificationEntity;
import com.nlmk.adp.db.repository.NotificationRepository;
import com.nlmk.adp.kafka.dto.NotificationDto;
import com.nlmk.adp.services.mapper.NotificationToDaoMapper;
import com.nlmk.adp.services.mapper.NotificationToDtoMapper;

/**
 * NotificationDaoImpl.
 */
@Service
@AllArgsConstructor
public class NotificationDaoImpl implements NotificationDao {

    private final NotificationToDtoMapper notificationDtoMapper;

    private final NotificationToDaoMapper notificationDaoMapper;

    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public NotificationEntity save(NotificationDto model) {
        var entity = notificationDaoMapper.mapDtoToEntity(model);

        var roles = entity.getNotificationRolesEntities();
        var emails = entity.getNotificationUserSuccessEntities();
        roles.forEach(i -> i.setNotification(entity));
        emails.forEach(i -> i.setNotification(entity));

        return notificationRepository.save(entity);
    }

    @Override
    @Transactional
    public NotificationDto getById(UUID id) {
        var entity = notificationRepository.findById(id).orElseThrow();
        return notificationDtoMapper.mapToDto(entity);
    }

}
