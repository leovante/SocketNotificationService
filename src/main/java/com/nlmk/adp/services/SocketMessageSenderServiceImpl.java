package com.nlmk.adp.services;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nlmk.adp.db.repository.NotificationEmailRepository;
import com.nlmk.adp.db.repository.NotificationRepository;
import com.nlmk.adp.dto.StompAuthenticationToken;
import com.nlmk.adp.kafka.dto.NotificationDto;
import com.nlmk.adp.services.component.ActiveUserStore;
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

        activeUserStore.getUsersByTopic(startTopic, dto.emails()).stream()
                       .filter(i -> !checkIsMessageWasReadedByUser(dto.id(), i.getUser().getName()))
                       .forEach(usr -> convertAndSendToUser(
                               castDtoToMessage(dto),
                               (StompAuthenticationToken) usr.getUser().getPrincipal()));
    }

    /**
     * resendToNotReadedEmails.
     *
     * @param user
     *         user
     */
    @Override
    @Transactional
    public void resendToNotReadedWsUsers(StompAuthenticationToken user) {
        log.info("Send for user message, email: {}, topic: {}", user.getPrincipal().getName(), startTopic);

        var snapshots = notificationRepository.findActualByUserInfo(
                user.getName(),
                user.getAccount().getRoles(),
                10);

        if (snapshots.isEmpty()) {
            return;
        }

        var messages = snapshots.stream()
                                .map(notificationFromDaoMapper::mapToDto)
                                .toList();

        messages.forEach(msg -> convertAndSendToUser(
                castDtoToMessage(msg),
                user));
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
    private void convertAndSendToUser(String message, StompAuthenticationToken user) {
        template.convertAndSendToUser(
                user.getName(),
                startTopic,
                message);
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

}
