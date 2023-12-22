package com.nlmk.adp.services;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nlmk.adp.db.repository.NotificationEmailRepository;
import com.nlmk.adp.db.repository.NotificationRepository;
import com.nlmk.adp.dto.JwtAuthentication;
import com.nlmk.adp.kafka.dto.NotificationBaseDto;
import com.nlmk.adp.kafka.dto.NotificationDto;
import com.nlmk.adp.kafka.dto.ReadByUserEmailDto;
import com.nlmk.adp.kafka.dto.RoleDto;
import com.nlmk.adp.services.component.ActiveUserStore;
import com.nlmk.adp.services.mapper.NotificationRoleType;
import com.nlmk.adp.services.mapper.NotificationToDtoMapper;
import com.nlmk.adp.services.mapper.SocketDtoToUserSubscribeMapper;

/**
 * SocketMessageSenderImpl.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SocketMessageSenderServiceImpl implements SocketMessageSenderService {

    @Value("${websocket.topic.start:/topic/notification}")
    private String startTopic;

    private final ObjectMapper objectMapper;

    private final SimpMessagingTemplate template;

    private final ActiveUserStore activeUserStore;

    private final SocketDtoToUserSubscribeMapper socketDtoToUserMessageMapper;

    private final NotificationToDtoMapper notificationFromDaoMapper;

    private final NotificationEmailRepository notificationEmailRepository;

    private final NotificationRepository notificationRepository;

    /**
     * send.
     *
     * @param dto
     *         msg
     */
    @Override
    @Transactional
    public void send(NotificationDto dto) {
        log.info("Send for users message: {}, topic: {}, amount of users: {}",
                 dto, startTopic, activeUserStore.getUserNames().size());

        activeUserStore.getUsersByTopic(startTopic).stream()
                       .filter(i -> shouldBeSent(dto, i))
                       .forEach(usr -> convertAndSendToUser(
                               castDtoToMessage(dto),
                               (JwtAuthentication) usr.getUser().getPrincipal()));
    }

    /**
     * resendToNotReadedEmails.
     *
     * @param user
     *         user
     */
    @Override
    @Transactional
    public void resendToNotReadedWsUsers(JwtAuthentication user) {
        log.info("Send for user message, user name: {}, topic: {}", user.getName(), startTopic);

        var snapshots = notificationRepository.findActualByUserInfo(
                user.getName(),
                user.getRoles(),
                100
        );

        if (snapshots.isEmpty()) {
            return;
        }

        var msg = snapshots.stream()
                           .map(notificationFromDaoMapper::mapToBaseDto)
                           .sorted(Collections.reverseOrder())
                           .toList();

        convertAndSendToUser(
                castDtoToMessage(msg),
                user);
    }

    /**
     * send.
     *
     * @param message
     *         message
     * @param user
     *         user
     */
    @SneakyThrows
    private void convertAndSendToUser(String message, JwtAuthentication user) {
        log.info(
                "Send message by socket, user name: {}, topic: {}, msg: {}",
                user.getName(),
                startTopic,
                message
        );
        template.convertAndSendToUser(
                user.getName(),
                startTopic,
                message);
    }

    /**
     * shouldBeSent.
     *
     * @param dto
     *         dto
     * @param i
     *         i
     *
     * @return boolean
     */
    private boolean shouldBeSent(NotificationDto dto, SimpSession i) {
        var notifEmails = dto.readByUserEmails().stream().map(ReadByUserEmailDto::email).toList();
        var userEmail = i.getUser().getName();

        List<RoleDto> notifRoles = dto.roles();
        Set<String> userRoles = ((JwtAuthentication) i.getUser().getPrincipal()).getRoles();

        boolean isMatchedByEmail = isMatchedByEmail(notifEmails, userEmail);
        boolean isMatchedByRoles = isMatchedByRoles(notifRoles, userRoles);
        boolean isNotReadedByEmail = !checkIsMessageWasReadedByUser(dto.id(), i.getUser().getName());

        return (isMatchedByRoles || isMatchedByEmail) && isNotReadedByEmail;
    }

    /**
     * isMatchedByEmail.
     *
     * @param notifEmails
     *         notifEmails
     * @param userEmail
     *         userEmail
     *
     * @return boolean
     */
    private boolean isMatchedByEmail(List<String> notifEmails, String userEmail) {
        return notifEmails.contains(userEmail);
    }

    /**
     * isMatchedByRoles.
     *
     * @param notifRoles
     *         notifRoles
     * @param userRoles
     *         userRoles
     *
     * @return boolean
     */
    private boolean isMatchedByRoles(
            List<RoleDto> notifRoles,
            Set<String> userRoles
    ) {
        boolean isAccepted = notifRoles
                .stream()
                .filter(it -> it.roleType() == NotificationRoleType.ACCEPT)
                .anyMatch(it -> userRoles.contains(it.role()));
        boolean isRejected = notifRoles
                .stream()
                .filter(it -> it.roleType() == NotificationRoleType.REJECT)
                .anyMatch(it -> userRoles.contains(it.role()));
        return isAccepted && !isRejected;
    }

    /**
     * isReaded.
     *
     * @param uuid
     *         uuid
     * @param email
     *         email
     *
     * @return boolean
     */
    private boolean checkIsMessageWasReadedByUser(UUID uuid, String email) {
        return notificationEmailRepository.userIsRead(uuid, email) > 0;
    }

    /**
     * castDtoToMessage.
     *
     * @param dto
     *         dto
     *
     * @return String
     */
    @SneakyThrows
    private String castDtoToMessage(NotificationDto dto) {
        return objectMapper.writeValueAsString(List.of(socketDtoToUserMessageMapper.mapDtoToMessage(dto)));
    }

    /**
     * castDtoToMessage.
     *
     * @param dto
     *         dto
     *
     * @return String
     */
    @SneakyThrows
    private String castDtoToMessage(List<NotificationBaseDto> dto) {
        return objectMapper.writeValueAsString(dto);
    }

}
