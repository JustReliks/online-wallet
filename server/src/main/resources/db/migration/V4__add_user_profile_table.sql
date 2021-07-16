create table w_user_profile
(
    id serial not null,
    user_id int not null
        constraint w_user_profile_w_users_id_fk
            references w_users
            on update cascade on delete cascade,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    middle_name varchar(255),
    about text,
    url varchar(255),
    phone varchar(255),
    country varchar(255),
    language varchar(255)
);

create unique index w_user_profile_id_uindex
	on w_user_profile (id);

create unique index w_user_profile_user_id_uindex
	on w_user_profile (user_id);

alter table w_user_profile
    add constraint w_user_profile_pk
        primary key (id);

