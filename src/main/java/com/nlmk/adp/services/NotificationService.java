package com.nlmk.adp.services;

import com.nlmk.adp.kafka.dto.NotificationDto;
import nlmk.l3.mesadp.DbUserNotificationVer0;

public interface NotificationService {
    void send(NotificationDto body);

    void sendToKafka(NotificationDto body);

    void invalidate(DbUserNotificationVer0 body, String reason);
}
