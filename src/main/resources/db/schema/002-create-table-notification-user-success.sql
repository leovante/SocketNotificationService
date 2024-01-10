--liquibase formatted sql
--changeset nosenko_ks:002-create-table-notification-user-success

create table notification_user_success
(
    notification_id uuid         not null
        constraint notification_user_success_notification_fk
            references notification,
    email           varchar(255) not null,
    read_at         timestamp without time zone,
    constraint notification_email_pk primary key (notification_id, email)
);

comment on table notification_user_success is 'факт доставки уведомления пользователю';
comment on column notification_user_success.notification_id is 'идентификатор уведомления';
comment on column notification_user_success.email is 'email-идентификатор пользователя';
comment on column notification_user_success.read_at is 'дата прочтения';

create index notification_user_success_notification_id_idx
    on notification_user_success (notification_id);