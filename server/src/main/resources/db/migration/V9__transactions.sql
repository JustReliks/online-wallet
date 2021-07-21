create table w_transactions
(
    id serial not null,
    account_id int not null
        constraint w_transactions_w_accounts_id_fk
            references w_accounts,
    bill_id int not null
        constraint w_transactions_w_account_bills_id_fk
            references w_account_bills,
    category_id int default 0 not null,
    datetime timestamp default now(),
    quantity double precision default 0,
    column_7 int
);

create unique index w_transactions_id_uindex
	on w_transactions (id);

alter table w_transactions
    add constraint w_transactions_pk
        primary key (id);

