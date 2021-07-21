create table w_types
(
    id serial not null,
    name varchar(255) not null,
    icon bytea
);

create unique index w_types_id_uindex
	on w_types (id);

alter table w_types
    add constraint w_types_pk
        primary key (id);



create table w_account_type
(
    id serial not null,
    account_id int not null
        constraint w_account_type_w_accounts_id_fk
            references w_accounts,
    type_id int
);

create unique index w_account_type_account_id_uindex
	on w_account_type (account_id);

create unique index w_account_type_id_uindex
	on w_account_type (id);

alter table w_account_type
    add constraint w_account_type_pk
        primary key (id);

