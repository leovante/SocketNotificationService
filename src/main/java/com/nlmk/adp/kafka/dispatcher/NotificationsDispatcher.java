package com.nlmk.adp.kafka.dispatcher;

import com.nlmk.adp.dto.DbUserNotificationVer0;

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
