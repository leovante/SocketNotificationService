package com.nlmk.adp.kafka.dispatcher;

import nlmk.l3.mesadp.DbUserNotificationVer0;

/**
 * NotificationsDispatcher.
 */
public interface NotificationsDispatcher {

    /**
     * dispatch.
     *
     * @param msg msg
     */
    void dispatch(DbUserNotificationVer0 msg);

}
