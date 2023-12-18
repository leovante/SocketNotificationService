package com.nlmk.adp.db.dao;

import java.util.UUID;

import com.nlmk.adp.db.entity.NotificationEntity;
import com.nlmk.adp.kafka.dto.NotificationDto;

/**
 * NotificationDao.
 */
public interface NotificationDao {

    /**
     * save.
     *
     * @param model
     *         model
     *
     * @return NotificationEntity
     */
    NotificationEntity save(NotificationDto model);

    /**
     * by id.
     *
     * @param id
     *         id.
     *
     * @return dto.
     */
    NotificationDto getById(UUID id);

}
