package com.orderengine.deliver.deliverservice.repository;

import com.orderengine.deliver.deliverservice.model.dto.DeliveryOrderDetailsResponseDto;
import com.orderengine.deliver.deliverservice.model.entity.DeliveryOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryOrderRepository extends JpaRepository<DeliveryOrder, Long> {
    List<DeliveryOrder> findAllByCourierIdIsNull();

    List<DeliveryOrder> findAllByUserLogin(String userLogin);

    @Query("""
            select new com.orderengine.deliver.deliverservice.model.dto.DeliveryOrderDetailsResponseDto(
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
    Optional<DeliveryOrderDetailsResponseDto> findDeliveryOrderById(@Param("orderId") Long orderId, @Param("userLogin") String userLogin);

}
