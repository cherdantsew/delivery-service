package com.orderengine.deliver.deliverservice.repository;

import com.orderengine.deliver.deliverservice.model.entity.DeliveryOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryOrderRepository extends JpaRepository<DeliveryOrder, Long> {
    List<DeliveryOrder> findAllByCourierIdIsNull();
}
