alter table w_transactions
    add user_id int not null;

alter table w_transactions
    add constraint w_transactions_w_users_id_fk
        foreign key (user_id) references w_users;

