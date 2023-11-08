package com.nlmk.adp.services;

import com.nlmk.adp.db.entity.NotificationEntity;
import com.nlmk.adp.kafka.dto.NotificationDto;

public interface NotificationDaoService {
    NotificationEntity save(NotificationDto model);

}
