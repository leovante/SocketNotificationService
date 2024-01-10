--liquibase formatted sql
--changeset nosenko_ks:001-create-table-notification-roles

create table notification_roles
(
    notification_id uuid         not null
        constraint notification_roles_notification_fk
            references notification,
    role            varchar(255) not null,
    role_type       varchar(255) not null,
    constraint notification_role_pk primary key (notification_id, role)
);

comment on table notification_roles is 'роли, назначенные уведомлениям';
comment on column notification_roles.notification_id is 'идентификатор уведомления';
comment on column notification_roles.role is 'название роли';
comment on column notification_roles.role_type is 'тип роли';

create index notification_roles_notification_id_idx
    on notification_roles (notification_id);