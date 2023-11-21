package com.nlmk.adp.services;

import com.nlmk.adp.db.entity.NotificationEntity;
import com.nlmk.adp.kafka.dto.NotificationDto;

/**
 * NotificationDaoService.
 */
public interface NotificationDaoService {

    /**
     * save.
     *
     * @param model model
     * @return NotificationEntity
     */
    NotificationEntity save(NotificationDto model);

}
