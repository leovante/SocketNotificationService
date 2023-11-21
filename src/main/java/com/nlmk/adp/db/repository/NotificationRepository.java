package com.nlmk.adp.db.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nlmk.adp.db.entity.NotificationEntity;

/**
 * NotificationRepository.
 */
public interface NotificationRepository extends JpaRepository<NotificationEntity, UUID> {

}
