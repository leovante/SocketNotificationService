package com.nlmk.adp.db.repository;

import com.nlmk.adp.db.entity.NotificationEmailPk;
import com.nlmk.adp.db.entity.NotificationRolesEntity;
import com.nlmk.adp.db.entity.NotificationRolesPk;
import com.nlmk.adp.db.entity.NotificationUserSuccessEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationEmailRepository extends JpaRepository<NotificationUserSuccessEntity, NotificationEmailPk> {

}
