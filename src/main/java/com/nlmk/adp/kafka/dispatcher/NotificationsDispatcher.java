package com.nlmk.adp.kafka.dispatcher;

import com.nlmk.adp.dto.DbUserNotificationVer0;

public interface NotificationsDispatcher {
    void dispatch(DbUserNotificationVer0 msg);
}
