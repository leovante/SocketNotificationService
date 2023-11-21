package com.nlmk.adp.services;

import com.nlmk.adp.dto.DbUserNotificationVer0;

/**
 * RemoteNotificationService.
 */
public interface RemoteNotificationService {

    /**
     * send.
     *
     * @param body body
     */
    void send(DbUserNotificationVer0 body);

}
