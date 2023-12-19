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
                    select distinct notif.*
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
                    where notif.expired_at > now()
                       and (nus.email is not null
                       or (accroles is not null and rejroles is null))
                    ORDER BY notif.kafka_dt desc
                    limit :limitElem
                    """,
            nativeQuery = true
    )
    List<NotificationEntity> findBacklogByUserInfo(
            @Param("useremail") String useremail,
            @Param("userroles") Set<String> userroles,
            @Param("limitElem") Integer limitElem
    );

    /**
     * Для вывода пользователю в вебсокеты.
     *
     * @param useremail
     *         почта.
     * @param userroles
     *         роли.
     * @param limitElem
     *         лимит для вебсокета.
     *
     * @return уведомления.
     */
    @Query(
            value = """
                    select distinct notif.*
                    from notification notif
                             left join notification_roles accroles
                                       on notif.id = accroles.notification_id
                                           and accroles.role_type = 'ACCEPT'
                                           and accroles.role in :userroles
                             left join notification_roles rejroles
                                       on notif.id = rejroles.notification_id
                                           and rejroles.role_type = 'REJECT'
                                           and rejroles.role in :userroles
                             left join notification_user_success nusnotread
                                       on notif.id = nusnotread.notification_id
                                           and nusnotread.email = :useremail
                                           and nusnotread.read_at is null
                             left join notification_user_success nusread
                                       on notif.id = nusread.notification_id
                                           and nusread.email = :useremail
                                           and nusread.read_at is not null
                    where notif.expired_at > now()
                      and nusread.email is null
                      and ((nusnotread.email is not null)
                        or (accroles is not null
                            and rejroles is null))
                    order by notif.kafka_dt desc
                    limit :limitElem
                    """,
            nativeQuery = true
    )
    List<NotificationEntity> findActualByUserInfo(
            @Param("useremail") String useremail,
            @Param("userroles") Set<String> userroles,
            @Param("limitElem") Integer limitElem
    );

}
