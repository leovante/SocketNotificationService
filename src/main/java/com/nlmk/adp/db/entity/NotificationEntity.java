package com.nlmk.adp.db.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * NotificationEntity.
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notification")
public class NotificationEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "expired_at")
    private OffsetDateTime expiredAt;

    @Column(name = "body")
    @Length(max = 100)
    private String body;

    @Column(name = "header")
    @Length(max = 100)
    private String header;

    @Column(name = "href")
    @Length(max = 100)
    private String href;

    @Column(name = "kafka_dt")
    private OffsetDateTime kafkaDt;

    @OneToMany(mappedBy = "primaryKey.notificationId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private Set<NotificationRolesEntity> notificationRolesEntities;

    @OneToMany(mappedBy = "primaryKey.notificationId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private Set<NotificationUserSuccessEntity> notificationUserSuccessEntities;

}
