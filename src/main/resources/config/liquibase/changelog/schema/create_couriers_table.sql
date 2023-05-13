create table couriers
(
    id             bigint primary key not null,
    login          varchar            not null,
    contact_info   varchar,
    courier_status varchar            not null
);