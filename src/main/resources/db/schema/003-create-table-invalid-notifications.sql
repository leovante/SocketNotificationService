--liquibase formatted sql
--changeset nosenko_ks:003-create-table-invalid-notifications

create table invalid_notifications
(
    id            uuid                        not null
        constraint invalid_notifications_pkey
            primary key,
    created_at    timestamp without time zone not null,
    updated_at    timestamp without time zone not null,
    raw_message   text,
    error_message text
);

comment on table invalid_notifications is 'невалидные уведомления';
comment on column invalid_notifications.id is 'уникальный идентификатор';
comment on column invalid_notifications.created_at is 'дата создания';
comment on column invalid_notifications.updated_at is 'дата обновления';
comment on column invalid_notifications.raw_message is 'сообщение';
comment on column invalid_notifications.error_message is 'сообщение ошибки';
