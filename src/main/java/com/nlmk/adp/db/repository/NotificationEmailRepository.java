package com.nlmk.adp.db.repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nlmk.adp.db.entity.NotificationEmailPk;
import com.nlmk.adp.db.entity.NotificationUserSuccessEntity;

/**
 * NotificationEmailRepository.
 */
public interface NotificationEmailRepository extends JpaRepository<NotificationUserSuccessEntity, NotificationEmailPk> {

    /**
     * readedEmails.
     *
     * @param uuid
     *         uuid
     * @param email
     *         email
     *
     * @return Integer
     */
    @Query("""
            select count (u.primaryKey.email) from NotificationUserSuccessEntity u
            where u.primaryKey.notificationId = ?1
            and u.primaryKey.email = ?2
            and u.readAt IS NOT NULL
            """)
    Integer userIsRead(UUID uuid, String email);

    /**
     * findUnreadMessagesByUuid.
     *
     * @param ids
     *         ids
     * @param useremail
     *         useremail
     * @param userroles
     *         userroles
     * @param limitElem
     *         limitElem
     *
     * @return List
     */
    @Query(
            value = """
                    select distinct nutsuccess.*
                    from notification_user_success nutsuccess
                             left join notification_roles accroles
                                       on nutsuccess.notification_id = accroles.notification_id
                                           and accroles.role_type = 'ACCEPT'
                                           and accroles.role in :userroles
                             left join notification_roles rejroles
                                       on nutsuccess.notification_id = rejroles.notification_id
                                           and rejroles.role_type = 'REJECT'
                                           and rejroles.role in :userroles
                    where nutsuccess.notification_id in :ids
                      and nutsuccess.read_at is null
                      and nutsuccess.email = :useremail
                      and ((nutsuccess.email is not null)
                        or (accroles is not null
                            and rejroles is null))
                    limit :limitElem
                    """,
            nativeQuery = true
    )
    List<NotificationUserSuccessEntity> findUnreadMessagesByUuid(
            @Param("ids") Set<UUID> ids,
            @Param("useremail") String useremail,
            @Param("userroles") Set<String> userroles,
            @Param("limitElem") Integer limitElem
    );

}
