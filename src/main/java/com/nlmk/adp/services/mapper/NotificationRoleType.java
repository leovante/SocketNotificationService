package com.nlmk.adp.services.mapper;

/**
 * Тип роли пользователя в контексте маршрутизации уведомлений.
 */
public enum NotificationRoleType {

    /**
     * Уведомление должно прийти.
     */
    ACCEPT,

    /**
     * Уведомление не должно приходить.
     */
    REJECT;

}
