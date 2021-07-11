create table w_roles
(
    id   serial not null
        CONSTRAINT bc_roles_pk
            PRIMARY KEY,
    name varchar not null
);

insert into w_roles(id, name)
VALUES (1, 'USER'),
       (2, 'ADMIN');


create table w_users_roles(
                               id serial CONSTRAINT w_users_roles_pk primary key not null,
                               user_id integer,
                               role_id integer,
                               foreign key (user_id) references w_users (id),
                               foreign key (role_id) references w_roles (id)
)
