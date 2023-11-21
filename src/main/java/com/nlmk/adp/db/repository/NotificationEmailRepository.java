package com.nlmk.adp.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nlmk.adp.db.entity.NotificationEmailPk;
import com.nlmk.adp.db.entity.NotificationUserSuccessEntity;

/**
 * NotificationEmailRepository.
 */
public interface NotificationEmailRepository extends JpaRepository<NotificationUserSuccessEntity, NotificationEmailPk> {

}
