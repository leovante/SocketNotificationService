--liquibase formatted sql
--changeset nosenko_ks:001-add-notification-number

create sequence notification_ordinal_number_seq
    start with 1
    increment by 1
    cache 10;

alter table notification
    add column ordinal_number bigint not null
        default nextval('notification_ordinal_number_seq');

alter sequence notification_ordinal_number_seq
    owned by notification.ordinal_number;