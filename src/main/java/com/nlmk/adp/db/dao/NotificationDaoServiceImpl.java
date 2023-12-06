package com.nlmk.adp.db.dao;

import java.util.UUID;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nlmk.adp.db.entity.NotificationEntity;
import com.nlmk.adp.db.repository.NotificationEmailRepository;
import com.nlmk.adp.db.repository.NotificationRepository;
import com.nlmk.adp.db.repository.NotificationRoleRepository;
import com.nlmk.adp.kafka.dto.NotificationDto;
import com.nlmk.adp.services.mapper.NotificationToDaoMapper;

/**
 * NotificationDaoServiceImpl.
 */
@Service
@AllArgsConstructor
public class NotificationDaoServiceImpl implements NotificationDaoService {

    private final NotificationToDaoMapper notificationDaoMapper;

    private final NotificationRepository notificationRepository;

    private final NotificationRoleRepository notificationRoleRepository;

    private final NotificationEmailRepository notificationEmailRepository;

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
        return notificationDaoMapper.mapToDto(entity);
    }

}
