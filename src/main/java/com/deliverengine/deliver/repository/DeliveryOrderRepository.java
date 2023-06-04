package com.deliverengine.deliver.repository;

import com.deliverengine.deliver.model.dto.DeliveryOrderDetailsResponseDto;
import com.deliverengine.deliver.model.entity.DeliveryOrder;
import com.deliverengine.deliver.model.enumeration.OrderStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryOrderRepository extends JpaRepository<DeliveryOrder, Long> {
    List<DeliveryOrder> findAllByCourierIdIsNull();

    Optional<DeliveryOrder> findByIdAndCourierLogin(Long id, String login);

    List<DeliveryOrder> findAllByUserLogin(String userLogin);

    @Query("""
            select new com.deliverengine.deliver.model.dto.DeliveryOrderDetailsResponseDto(
            pdo.userLogin,
            pdo.deliveryCost,
            pdo.destination,
            pdo.createdAt,
            pdo.orderStatus,
            pdo.deliveredAt,
            c.login,
            c.contactInfo
            ) from DeliveryOrder pdo
                left join Courier c on pdo.courier.id = c.id
            where pdo.userLogin = :userLogin
            and pdo.id = :orderId
            """)
    Optional<DeliveryOrderDetailsResponseDto> findDeliveryOrderByIdAndUserLogin(@Param("orderId") Long orderId, @Param("userLogin") String userLogin);



    List<DeliveryOrder> findAllByIdAndCourierLogin(Long orderId, String courierLogin);

    List<DeliveryOrder> findAllByCourierLoginAndOrderStatusNotIn(String login, List<OrderStatus> orderStatuses);
}
