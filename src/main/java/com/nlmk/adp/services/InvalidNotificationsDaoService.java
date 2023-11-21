package com.nlmk.adp.services;

import com.fasterxml.jackson.databind.JsonNode;

import com.nlmk.adp.db.entity.InvalidNotificationsEntity;

/**
 * InvalidNotificationsDaoService.
 */
public interface InvalidNotificationsDaoService {

    /**
     * save.
     *
     * @param msg msg
     * @param reason reason
     * @return InvalidNotificationsEntity
     */
    InvalidNotificationsEntity save(JsonNode msg, String reason);

}
