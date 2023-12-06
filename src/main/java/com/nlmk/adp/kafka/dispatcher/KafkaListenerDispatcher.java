package com.nlmk.adp.kafka.dispatcher;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.nlmk.adp.kafka.dto.NotificationDto;
import com.nlmk.adp.services.NotificationService;
import nlmk.EnumOp;

/**
 * Первично преобразует, валидирует и перенаправляет сообщения из кафки на обработчики.
 */
@Service
@AllArgsConstructor
@Validated
public class KafkaListenerDispatcher {

    private final NotificationService notificationService;

    /**
     * Обработать сообщение с уведомлением.
     *
     * @param operation
     *         операция из кафки (i/d/u).
     * @param notification
     *         уведомление.
     */
    public void dispatch(
            EnumOp operation,
            @Valid NotificationDto notification
    ) {
        switch (operation) {
            case I -> notificationService.send(notification);
            default -> throw new RuntimeException("Kafka operation " + operation + " is not supported");
        }
    }

}