--liquibase formatted sql
--changeset nosenko_ks:000-create-table-notification

create sequence notification_ordinal_number_seq
    start with 1
    increment by 1
    cache 10;

create table notification
(
    id             uuid                        not null
        constraint notification_pkey
            primary key,
    created_at     timestamp without time zone not null,
    updated_at     timestamp without time zone not null,
    expired_at     timestamp without time zone not null,
    body           text                        not null,
    header         text                        not null,
    href           text                        not null,
    ts             timestamp without time zone not null,
    ordinal_number bigint                      not null
        default nextval('notification_ordinal_number_seq')
);

alter sequence notification_ordinal_number_seq
    owned by notification.ordinal_number;

comment on table notification is 'уведомления';
comment on column notification.id is 'уникальный идентификатор';
comment on column notification.created_at is 'дата создания';
comment on column notification.updated_at is 'дата обновления';
comment on column notification.expired_at is 'дата завершения';
comment on column notification.body is 'сообщение уведомления';
comment on column notification.header is 'заголовок уведомления';
comment on column notification.href is 'ссылка для перехода';
comment on column notification.ts is 'дата сообщения из kafka';
comment on column notification.ordinal_number is 'порядковый номер уведомления';

create index notification_created_at_idx
    on notification (created_at);
