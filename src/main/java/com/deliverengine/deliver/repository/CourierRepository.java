package com.deliverengine.deliver.repository;

import com.deliverengine.deliver.model.enumeration.CourierStatus;
import com.deliverengine.deliver.model.entity.Courier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourierRepository extends JpaRepository<Courier, Long> {

    List<Courier> findAllByCourierStatusIs(CourierStatus courierStatus);
}
