create or replace view delivery_orders_couriers_view as
(
select pdo.id,
       pdo.user_login,
       pdo.destination,
       pdo.created_at,
       pdo.order_status,
       pdo.delivered_at,
       c.login,
       c.contact_info,
       c.courier_status
from parcel_delivery_order pdo
         left join couriers c on pdo.courier_id = couriers.id
    );