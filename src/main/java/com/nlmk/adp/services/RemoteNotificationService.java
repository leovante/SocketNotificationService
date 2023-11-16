package com.nlmk.adp.services;

import com.nlmk.adp.dto.DbUserNotificationVer0;

public interface RemoteNotificationService {
    void send(DbUserNotificationVer0 body);
}
