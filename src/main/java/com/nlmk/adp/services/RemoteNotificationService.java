package com.nlmk.adp.services;

import nlmk.l3.mesadp.DbUserNotificationVer0;

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
