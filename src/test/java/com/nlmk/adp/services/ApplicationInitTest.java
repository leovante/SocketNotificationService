package com.nlmk.adp.services;

import java.util.List;
import java.util.UUID;

import jakarta.validation.ClockProvider;
import jakarta.validation.ConstraintValidatorContext;
import lombok.Getter;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.nlmk.adp.BaseSpringBootTest;
import com.nlmk.adp.db.dao.InvalidNotificationsDao;
import com.nlmk.adp.db.repository.InvalidNotificationsRepository;
import com.nlmk.adp.db.repository.NotificationRepository;
import com.nlmk.adp.dto.NotificationCheck;
import com.nlmk.adp.kafka.listeners.NotificationListener;
import com.nlmk.adp.services.mapper.KafkaMessageToDtoMapper;
import nlmk.l3.mesadp.DbUserNotificationVer0;

class ApplicationInitTest extends BaseSpringBootTest {

    @Autowired
    private NotificationListener notificationListener;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private InvalidNotificationsDao invalidNotificationsDao;

    @Autowired
    private KafkaMessageToDtoMapper messageToDtoMapper;

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

        notificationListener.handleNotificationMessage(payload);
        var result = notificationRepository.findById(uuid).get();

        SoftAssertions.assertSoftly(it -> {
            it.assertThat(result.getHref()).isEqualTo("/dischargeVehiclesV2");
            it.assertThat(result.getBody()).isEqualTo(payload.getData().getBody());
            it.assertThat(result.getHeader()).isEqualTo(payload.getData().getHeader());
            it.assertThat(result.getCreatedAt()).isNotNull();
            it.assertThat(result.getUpdatedAt()).isNotNull();
            it.assertThat(result.getExpiredAt()).isNotNull();
            it.assertThat(result.getKafkaTs()).isNotNull();

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

        invalidNotificationsDao.saveNew(payload, error_text);
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

    @Test
    void kafkaIFullnvalidMessageTest() {
        var payload = getResource(
                "/kafka/notification_real_invalid.json",
                DbUserNotificationVer0.class);

        var uuid = UUID.randomUUID();
        payload.getPk().setId(uuid.toString());

        Context ctx = new Context();
        var snapshot = messageToDtoMapper.mapDataToDto(payload);
        new NotificationCheck.DbUserNotificationVer0Validator().isValid(snapshot, ctx);

        Assertions.assertThat(ctx.error_text).contains(List.of(
                "header should have at least 3 letter",
                "body should have at least 3 letter",
                "acceptRoles and rejectRoles should not cross:  admin",
                "expireAt date should be after now"
        ));
    }

    @Getter
    class Context implements ConstraintValidatorContext {

        private String error_text;

        @Override
        public void disableDefaultConstraintViolation() {
        }

        @Override
        public String getDefaultConstraintMessageTemplate() {
            return null;
        }

        @Override
        public ClockProvider getClockProvider() {
            return null;
        }

        @Override
        public ConstraintViolationBuilder buildConstraintViolationWithTemplate(String s) {
            error_text = s;
            return new BuilderImpl();
        }

        @Override
        public <T> T unwrap(Class<T> aClass) {
            return null;
        }

        class BuilderImpl implements ConstraintViolationBuilder {

            @Override
            public NodeBuilderDefinedContext addNode(String s) {
                return null;
            }

            @Override
            public NodeBuilderCustomizableContext addPropertyNode(String s) {
                return null;
            }

            @Override
            public LeafNodeBuilderCustomizableContext addBeanNode() {
                return null;
            }

            @Override
            public ContainerElementNodeBuilderCustomizableContext addContainerElementNode(String s,
                                                                                          Class<?> aClass,
                                                                                          Integer integer) {
                return null;
            }

            @Override
            public NodeBuilderDefinedContext addParameterNode(int i) {
                return null;
            }

            @Override
            public ConstraintValidatorContext addConstraintViolation() {
                return null;
            }

        }

    }

}
