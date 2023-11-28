package com.nlmk.adp;

import java.util.List;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.nlmk.adp.db.repository.InvalidNotificationsRepository;
import com.nlmk.adp.db.repository.NotificationRepository;
import com.nlmk.adp.kafka.listeners.NotificationListener;
import com.nlmk.adp.services.NotificationService;
import nlmk.l3.mesadp.DbUserNotificationVer0;

class ApplicationInitTest extends BaseSpringBootTest {

    @Autowired
    private NotificationListener notificationListener;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private InvalidNotificationsRepository invalidNotificationsRepository;

    @Test
    @Transactional
    void kafkaValidMessageTest() {
        var payload = getResource(
                "/kafka/notification_real.json",
                DbUserNotificationVer0.class);
        var uuid = UUID.randomUUID();
        payload.getPk().setId(uuid.toString());

        notificationListener.handleNotificationMessage(payload, null, null);
        var result = notificationRepository.findById(uuid).get();

        SoftAssertions.assertSoftly(it -> {
            it.assertThat(result.getHref()).isEqualTo(payload.getData().getHref());
            it.assertThat(result.getBody()).isEqualTo(payload.getData().getBody());
            it.assertThat(result.getHeader()).isEqualTo(payload.getData().getHeader());
            it.assertThat(result.getCreatedAt()).isNotNull();
            it.assertThat(result.getUpdatedAt()).isNull();
            it.assertThat(result.getExpiredAt()).isNotNull();
            it.assertThat(result.getKafkaDt()).isNotNull();

            var rolesResult = result.getNotificationRolesEntities().stream().toList();
            it.assertThat(rolesResult)
              .extracting("primaryKey.notificationId")
              .contains(uuid);
            it.assertThat(rolesResult)
              .extracting("primaryKey.role")
              .contains(payload.getData().getAcceptRoles().get(0));

            var userResult = result.getNotificationUserSuccessEntities().stream().toList();
            it.assertThat(rolesResult)
              .extracting("primaryKey.notificationId")
              .contains(uuid);
            it.assertThat(userResult)
              .extracting("primaryKey.email")
              .isEqualTo(payload.getData().getAcceptEmails());
            it.assertThat(userResult.get(0).getReadAt()).isNull();
        });
    }

    @Test
    void kafkaInvalidMessageTest() {
        var error_text = "error text message";
        var payload = getResource(
                "/kafka/notification_real.json",
                DbUserNotificationVer0.class);
        var uuid = UUID.randomUUID();
        payload.getPk().setId(uuid.toString());
        payload.getData().setHeader(null);
        payload.getData().setBody(null);
        payload.getData().setAcceptEmails(null);
        payload.getData().setHref("test.nlmk1.com");
        payload.getData().setAcceptEmails(List.of("admin", "operator"));
        payload.getData().setRejectRoles(List.of("operator"));

        notificationService.invalidate(payload, error_text);
        var invalidTableResult = invalidNotificationsRepository.findAll()
                                                               .stream()
                                                               .filter(f -> f.getRawMessage().findValue("id").toString()
                                                                             .replace("\"", "")
                                                                             .equals(uuid.toString()))
                                                               .findFirst().get();

        Assertions.assertThat(invalidTableResult.getErrorMessage()).contains(error_text);

        var validTableResult = notificationRepository.findById(uuid);
        Assertions.assertThat(validTableResult).isEmpty();
    }

}
