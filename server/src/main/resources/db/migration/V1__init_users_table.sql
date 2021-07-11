CREATE TABLE w_users
(
    id           serial           NOT NULL
        CONSTRAINT w_users_pk
            PRIMARY KEY,
    username     varchar(32)      NOT NULL,
    email        varchar(255)     NOT NULL,
    password     varchar(60)      NOT NULL,
    balance      double precision NOT NULL,
    uuid         char(36)         NOT NULL,
    access_token char(36)   DEFAULT NULL,
    currency     varchar(3) DEFAULT 'RUB',
    created_at   timestamp        NOT NULL
)
