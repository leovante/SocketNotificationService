package com.nlmk.adp.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.nlmk.adp.db.entity.InvalidNotificationsEntity;

public interface InvalidNotificationsDaoService {

    InvalidNotificationsEntity save(JsonNode msg, String reason);

}
