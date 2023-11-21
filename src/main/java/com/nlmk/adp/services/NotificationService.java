package com.nlmk.adp.services;

import com.nlmk.adp.kafka.dto.NotificationDto;
import nlmk.l3.mesadp.DbUserNotificationVer0;

/**
 * NotificationService.
 */
public interface NotificationService {

    /**
     * send.
     *
     * @param body body
     */
    void send(NotificationDto body);

    /**
     * sendToKafka.
     *
     * @param body body
     */
    void sendToKafka(NotificationDto body);

    /**
     * invalidate.
     *
     * @param body body
     * @param reason reason
     */
    void invalidate(DbUserNotificationVer0 body, String reason);

}
