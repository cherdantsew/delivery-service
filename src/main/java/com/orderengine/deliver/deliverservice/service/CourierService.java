package com.orderengine.deliver.deliverservice.service;

import com.orderengine.deliver.deliverservice.model.entity.Courier;
import com.orderengine.deliver.deliverservice.model.enumeration.CourierStatus;
import com.orderengine.deliver.deliverservice.repository.CourierRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourierService {

    private CourierRepository repository;

    public CourierService(CourierRepository repository) {
        this.repository = repository;
    }

    public List<Courier> findAllByCourierStatus(CourierStatus status) {
        return repository.findAllByCourierStatusIs(status);
    }

    public void save(Courier courier) {
        repository.save(courier);
    }
}
