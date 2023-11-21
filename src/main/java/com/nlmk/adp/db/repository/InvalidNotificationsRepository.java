package com.nlmk.adp.db.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nlmk.adp.db.entity.InvalidNotificationsEntity;

/**
 * InvalidNotificationsRepository.
 */
public interface InvalidNotificationsRepository extends JpaRepository<InvalidNotificationsEntity, UUID> {

}
