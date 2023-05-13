create table users
(
    login           varchar not null primary key,
    contact_info    varchar,
    account_balance bigint default 0
);
