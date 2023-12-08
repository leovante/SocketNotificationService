package com.nlmk.adp.db.repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nlmk.adp.db.entity.NotificationEntity;

/**
 * NotificationRepository.
 */
public interface NotificationRepository extends JpaRepository<NotificationEntity, UUID> {

    /**
     * findAllNotificationbyRole.
     *
     * @param userroles
     *         valid role list
     * @param useremail
     *         useremail
     * @param limitElem
     *         max size list
     *
     * @return List of NotificationBaseDto
     */
    @Query(
            value = """
                    select notif.*
                    from notification notif
                             left join notification_roles accroles
                                       on notif.id = accroles.notification_id
                                           and accroles.role_type = 'ACCEPT'
                                           and accroles.role in :userroles
                             left join notification_roles rejroles
                                       on notif.id = rejroles.notification_id
                                           and rejroles.role_type = 'REJECT'
                                           and rejroles.role in :userroles
                             left join notification_user_success nus
                                       on notif.id = nus.notification_id
                                           and nus.email = :useremail
                    where nus is not null
                       or (accroles is not null
                        and rejroles is null) ORDER BY notif.created_at desc limit :limitElem
                        """,
            nativeQuery = true
    )
    List<NotificationEntity> findAllByUserInfo(
            @Param("userroles") Set<String> userroles,
            @Param("limitElem") Integer limitElem,
            @Param("useremail") String useremail
    );

}
