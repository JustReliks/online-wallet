alter table w_users
    add jwt_created_time_hash varchar;

create table w_users_totp
(
    id               serial not null,
    username         varchar,
    secret_key       varchar,
    validation_code int,
    scratch_codes    integer[],
    need_regenerate boolean default false
);

create
    unique index users_totp_id_uindex
    on w_users_totp (id);

alter table w_users_totp
    add constraint users_totp_pk
        primary key (id);

alter table w_users
    add enable_two_factor_auth boolean default false;

create table restore_access_tokens
(
    id serial not null,
    user_id int
        constraint restore_access_tokens_bc_users_id_fk
            references w_users,
    token varchar,
    expiry_date timestamp
);

create unique index restore_access_tokens_id_uindex
    on restore_access_tokens (id);

alter table restore_access_tokens
    add constraint restore_access_tokens_pk
        primary key (id);



