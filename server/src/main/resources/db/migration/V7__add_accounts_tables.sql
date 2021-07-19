create table w_accounts
(
    id serial not null,
    user_id int not null
        constraint w_accounts_w_users_id_fk
            references w_users
            on delete cascade,
    name int,
    description int,
    created_at timestamp default now(),
    last_transaction int,
    icon bytea
);

create unique index w_accounts_id_uindex
	on w_accounts (id);

alter table w_accounts
    add constraint w_accounts_pk
        primary key (id);

create table w_currencies
(
    id serial not null,
    short_name varchar(3) not null,
    long_name varchar(255)
);

create unique index w_currencies_id_uindex
	on w_currencies (id);

alter table w_currencies
    add constraint w_currencies_pk
        primary key (id);


create table w_account_bills
(
    id serial not null,
    account_id int not null
        constraint w_account_bills_w_accounts_id_fk
            references w_accounts,
    currency_id int not null
        constraint w_account_bills_w_currencies_id_fk
            references w_currencies (id),
    balance double precision default 0 not null
);

create unique index w_account_bills_id_uindex
	on w_account_bills (id);

alter table w_account_bills
    add constraint w_account_bills_pk
        primary key (id);

