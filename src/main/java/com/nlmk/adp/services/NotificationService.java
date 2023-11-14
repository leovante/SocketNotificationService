package com.nlmk.adp.services;

import com.nlmk.adp.dto.DbUserNotificationVer0;
import com.nlmk.adp.kafka.dto.NotificationDto;

public interface NotificationService {
    void send(NotificationDto body);
    void invalidate(DbUserNotificationVer0 body, String reason);
}
