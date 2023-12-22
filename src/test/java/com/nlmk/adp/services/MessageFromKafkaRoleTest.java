package com.nlmk.adp.services;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;

import com.nlmk.adp.BaseSpringBootTest;
import com.nlmk.adp.dto.JwtAuthentication;
import com.nlmk.adp.kafka.listeners.NotificationListener;
import nlmk.l3.mesadp.DbUserNotificationVer0;

public class MessageFromKafkaRoleTest extends BaseSpringBootTest {

    private static final String TEST_DESTINATION = "/user/topic/notification";

    private static final String TEST_USER_ACCEPT_EMAIL = "nlmk@nlmk.com";

    private static final String TEST_USER_ACCEPT_ROLE = "user";

    private static final String TEST_USER_REJECT_ROLE = "fired";

    @Autowired
    private NotificationListener notificationListener;

    @MockBean
    private SimpUserRegistry simpUserRegistry;

    @MockBean
    private SimpMessagingTemplate template;

    @Captor
    private ArgumentCaptor<Object> valueCapture;

    @BeforeEach
    public void setup() {
        SimpSubscription simpSubscription = new LocalSimpSubscription(
                "1",
                TEST_DESTINATION,
                new LocalSimpSession(
                        "1",
                        new LocalSimpUser(
                                TEST_USER_ACCEPT_EMAIL,
                                new JwtAuthentication(
                                        null,
                                        Set.of(TEST_USER_ACCEPT_ROLE, TEST_USER_REJECT_ROLE),
                                        TEST_USER_ACCEPT_EMAIL,
                                        null))));
        Mockito.when(simpUserRegistry.findSubscriptions(ArgumentMatchers.any())).thenReturn(Set.of(simpSubscription));
    }

    @Test
    @DisplayName("Пользователь должен получить сообщение")
    void acceptEmailAcceptRoleTest() throws InterruptedException {
        var payload = getResource(
                "/kafka/notification_real.json",
                DbUserNotificationVer0.class);
        var uuid = UUID.randomUUID();

        payload.getPk().setId(uuid.toString());
        payload.getData().setAcceptEmails(List.of(TEST_USER_ACCEPT_EMAIL));
        payload.getData().setAcceptRoles(List.of(TEST_USER_ACCEPT_ROLE));

        notificationListener.handleNotificationMessage(payload);

        Thread.sleep(10000);
        Mockito.verify(template, Mockito.times(1))
               .convertAndSendToUser(
                       ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), valueCapture.capture());
        var val = (String) valueCapture.getValue();

        Assertions.assertThat(val).contains("Поезд 238194958 прибыл на станцию 298519851");
    }

    @Test
    @DisplayName("Пользователь должен получить сообщение без email")
    void emptyEmailAcceptRoleTest() throws InterruptedException {
        var payload = getResource(
                "/kafka/notification_real.json",
                DbUserNotificationVer0.class);
        var uuid = UUID.randomUUID();

        payload.getPk().setId(uuid.toString());
        payload.getData().setAcceptRoles(List.of(TEST_USER_ACCEPT_ROLE));

        notificationListener.handleNotificationMessage(payload);

        Thread.sleep(10000);
        Mockito.verify(template, Mockito.times(1))
               .convertAndSendToUser(
                       ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), valueCapture.capture());
        var val = (String) valueCapture.getValue();

        Assertions.assertThat(val).contains("Поезд 238194958 прибыл на станцию 298519851");
    }

    @Test
    @DisplayName("Пользователь не получит сообщение без email, если есть роль reject")
    void emptyEmailRejectRoleTest() throws InterruptedException {
        var payload = getResource(
                "/kafka/notification_real.json",
                DbUserNotificationVer0.class);
        var uuid = UUID.randomUUID();

        payload.getPk().setId(uuid.toString());
        payload.getData().setAcceptRoles(List.of(TEST_USER_ACCEPT_ROLE));
        payload.getData().setRejectRoles(List.of(TEST_USER_REJECT_ROLE));

        notificationListener.handleNotificationMessage(payload);

        Thread.sleep(10000);
        Mockito.verify(template, Mockito.times(0))
               .convertAndSendToUser(
                       ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), valueCapture.capture());
    }

    @RequiredArgsConstructor
    private static class LocalSimpSession implements SimpSession {

        private final String id;

        private final LocalSimpUser user;

        private final Map<String, SimpSubscription> subscriptions = new ConcurrentHashMap(4);

        public String getId() {
            return this.id;
        }

        public LocalSimpUser getUser() {
            return this.user;
        }

        public Set<SimpSubscription> getSubscriptions() {
            return new HashSet(this.subscriptions.values());
        }

    }

    @RequiredArgsConstructor
    private static class LocalSimpSubscription implements SimpSubscription {

        private final String id;

        private final String destination;

        private final LocalSimpSession session;

        public String getId() {
            return this.id;
        }

        public LocalSimpSession getSession() {
            return this.session;
        }

        public String getDestination() {
            return this.destination;
        }

    }

    @RequiredArgsConstructor
    private static class LocalSimpUser implements SimpUser {

        private final String name;

        private final Principal user;

        private final Map<String, SimpSession> userSessions = new ConcurrentHashMap(1);

        public String getName() {
            return this.name;
        }

        @Nullable
        public Principal getPrincipal() {
            return this.user;
        }

        public boolean hasSessions() {
            return !this.userSessions.isEmpty();
        }

        @Nullable
        public SimpSession getSession(@Nullable String sessionId) {
            return sessionId != null ? (SimpSession) this.userSessions.get(sessionId) : null;
        }

        public Set<SimpSession> getSessions() {
            return new HashSet(this.userSessions.values());
        }

    }

}
