package com.nlmk.adp.db.repository;

import com.nlmk.adp.db.entity.InvalidNotificationsEntity;
import com.nlmk.adp.db.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvalidNotificationsRepository extends JpaRepository<InvalidNotificationsEntity, UUID> {

}
