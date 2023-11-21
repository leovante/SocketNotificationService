package com.nlmk.adp.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nlmk.adp.db.entity.NotificationRolesEntity;
import com.nlmk.adp.db.entity.NotificationRolesPk;

/**
 * NotificationRoleRepository.
 */
public interface NotificationRoleRepository extends JpaRepository<NotificationRolesEntity, NotificationRolesPk> {

}
