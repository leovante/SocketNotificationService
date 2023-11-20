package com.nlmk.adp.kafka.dispatcher;

import nlmk.l3.mesadp.DbUserNotificationVer0;

public interface NotificationsDispatcher {
    void dispatch(DbUserNotificationVer0 msg);
}
