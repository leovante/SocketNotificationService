package com.nlmk.adp.db.repository;

import com.nlmk.adp.db.entity.NotificationEntity;
import com.nlmk.adp.db.entity.NotificationRolesEntity;
import com.nlmk.adp.db.entity.NotificationRolesPk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRoleRepository extends JpaRepository<NotificationRolesEntity, NotificationRolesPk> {

}
