package com.nlmk.adp.db.dao;

import com.fasterxml.jackson.databind.JsonNode;

import com.nlmk.adp.db.entity.InvalidNotificationsEntity;

/**
 * InvalidNotificationsDao.
 */
public interface InvalidNotificationsDao {

    /**
     * save.
     *
     * @param msg msg
     * @param reason reason
     * @return InvalidNotificationsEntity
     */
    InvalidNotificationsEntity save(JsonNode msg, String reason);

}
