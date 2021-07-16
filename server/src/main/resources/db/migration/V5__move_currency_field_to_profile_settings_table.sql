alter table w_users drop column currency;

alter table w_user_profile rename to w_user_settings;

alter table w_user_settings
    add currency varchar(3) default 'RUB' not null;