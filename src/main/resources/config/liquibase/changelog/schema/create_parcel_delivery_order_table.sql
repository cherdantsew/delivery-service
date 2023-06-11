create table parcel_delivery_order
(
    id            bigserial primary key not null,
    user_login    varchar               not null,
    courier_id    bigint,
    destination   varchar               not null,
    created_at    timestamptz default now(),
    delivered_at  timestamptz,
    order_status  varchar               not null,
    delivery_cost bigint                not null,
    constraint pdo_courier_fk foreign key (courier_id) references couriers (id)
);
