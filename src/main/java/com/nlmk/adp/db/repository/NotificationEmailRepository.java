package com.nlmk.adp.db.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nlmk.adp.db.entity.NotificationEmailPk;
import com.nlmk.adp.db.entity.NotificationUserSuccessEntity;

/**
 * NotificationEmailRepository.
 */
public interface NotificationEmailRepository extends JpaRepository<NotificationUserSuccessEntity, NotificationEmailPk> {

    /**
     * readedEmails.
     *
     * @param uuid uuid
     * @param email email
     * @return Integer
     */
    @Query("""
            select count (u.primaryKey.email) from NotificationUserSuccessEntity u
            where u.primaryKey.notificationId = ?1
            and u.primaryKey.email = ?2
            and u.readAt IS NOT NULL
            """)
    Integer userIsRead(UUID uuid, String email);

}
