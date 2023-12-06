package com.nlmk.adp.services;

import java.util.UUID;

import com.nlmk.adp.kafka.dto.NotificationDto;

/**
 * NotificationService.
 */
public interface NotificationService {

    /**
     * send.
     *
     * @param body
     *         body
     */
    void send(NotificationDto body);

    /**
     * sendV2.
     *
     * @param body
     *         body
     */
    void sendV2(NotificationDto body);

    /**
     * sendToKafka.
     *
     * @param body
     *         body
     */
    void sendToKafka(NotificationDto body);

    /**
     * invalidate.
     *
     * @param body
     *         body
     * @param reason
     *         reason
     */
    void invalidate(Object body, String reason);

    /**
     * ById.
     *
     * @param id
     *         id.
     *
     * @return notif.
     */
    NotificationDto getById(UUID id);

}
