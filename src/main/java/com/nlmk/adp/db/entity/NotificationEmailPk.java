package com.nlmk.adp.db.entity;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * NotificationEmailPk.
 */
@Setter
@Getter
@Embeddable
@ToString(exclude = "notificationId")
public class NotificationEmailPk implements Serializable {

    @Column(name = "notification_id")
    @EqualsAndHashCode.Exclude
    private UUID notificationId;

    @Column(name = "email")
    @Length(max = 100)
    private String email;

    /**
     * NotificationEmailPk.
     */
    public NotificationEmailPk() {}

    /**
     * NotificationEmailPk.
     *
     * @param notificationId
     *         notificationId
     * @param email
     *         email
     */
    public NotificationEmailPk(UUID notificationId, String email) {
        this.notificationId = notificationId;
        this.email = email;
    }

}
