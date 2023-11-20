package com.nlmk.adp.services;

import nlmk.l3.mesadp.DbUserNotificationVer0;

public interface RemoteNotificationService {
    void send(DbUserNotificationVer0 body);
}
