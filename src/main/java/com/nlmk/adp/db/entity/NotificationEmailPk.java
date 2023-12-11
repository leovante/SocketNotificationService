package com.nlmk.adp.db.entity;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class NotificationEmailPk implements Serializable {

    public static final int VARCHAR_FIELD_MAX_SIZE = 100;

    @Column(name = "notification_id")
    @EqualsAndHashCode.Exclude
    private UUID notificationId;

    @Column(name = "email")
    @Length(max = VARCHAR_FIELD_MAX_SIZE)
    private String email;

}
