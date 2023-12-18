package com.nlmk.adp.db.dao;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.nlmk.adp.db.entity.NotificationUserSuccessEntity;

/**
 * NotificationEmailDao.
 */
public interface NotificationEmailDao {

    /**
     * markAll.
     *
     * @param email
     *         email
     * @param uuids
     *         uuids
     *
     * @return List
     */
    List<NotificationUserSuccessEntity> markAllReadedByEmail(String email, Set<String> roles, Set<UUID> uuids);

}
