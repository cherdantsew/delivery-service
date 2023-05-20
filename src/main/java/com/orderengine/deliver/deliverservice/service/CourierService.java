package com.orderengine.deliver.deliverservice.service;

import com.orderengine.deliver.deliverservice.model.entity.Courier;
import com.orderengine.deliver.deliverservice.model.enumeration.CourierStatus;
import com.orderengine.deliver.deliverservice.repository.CourierRepository;
import jakarta.persistence.LockModeType;
import java.util.List;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;

@Service
public class CourierService {

    private CourierRepository repository;

    public CourierService(CourierRepository repository) {
        this.repository = repository;
    }

    public List<Courier> findAllByCourierStatus(CourierStatus status) {
        return repository.findAllByCourierStatusIs(status);
    }

    public Courier findById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public void save(Courier courier) {
        repository.save(courier);
    }
}
