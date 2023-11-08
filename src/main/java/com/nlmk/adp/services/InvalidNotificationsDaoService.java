package com.nlmk.adp.services;

import com.nlmk.adp.db.entity.InvalidNotificationsEntity;

public interface InvalidNotificationsDaoService {

    InvalidNotificationsEntity save(String msg);

}
