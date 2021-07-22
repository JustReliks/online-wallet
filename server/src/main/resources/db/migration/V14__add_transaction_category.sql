create table w_transaction_category
(
    id    serial       not null,
    code  varchar(255) not null,
    type  varchar(255) not null,
    title varchar(255),
    icon  bytea
);

create
unique index w_category_id_uindex
	on w_transaction_category (id);

alter table w_transaction_category
    add constraint w_category_pk
        primary key (id);

alter table w_transactions
    add constraint w_transactions_w_transaction_category_id_fk
        foreign key (category_id) references w_transaction_category (id);



