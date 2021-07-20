create table w_goal
(
    id serial not null,
    name varchar(255) not null,
    value double precision default 0,
    date timestamp not null,
    account_id int not null
        constraint w_goal_w_accounts_id_fk
            references w_accounts
);

create unique index w_goal_account_id_uindex
	on w_goal (account_id);

create unique index w_goal_id_uindex
	on w_goal (id);

alter table w_goal
    add constraint w_goal_pk
        primary key (id);

