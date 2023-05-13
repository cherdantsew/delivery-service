package com.orderengine.deliver.deliverservice.repository;

import com.orderengine.deliver.deliverservice.model.entity.Courier;
import com.orderengine.deliver.deliverservice.model.enumeration.CourierStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourierRepository extends JpaRepository<Courier, Long> {

    List<Courier> findAllByCourierStatusIs(CourierStatus courierStatus);
}
