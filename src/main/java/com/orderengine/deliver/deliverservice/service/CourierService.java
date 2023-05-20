package com.orderengine.deliver.deliverservice.service;

import com.orderengine.deliver.deliverservice.mapper.CourierMapper;
import com.orderengine.deliver.deliverservice.model.dto.CourierResponseDto;
import com.orderengine.deliver.deliverservice.model.entity.Courier;
import com.orderengine.deliver.deliverservice.model.enumeration.CourierStatus;
import com.orderengine.deliver.deliverservice.repository.CourierRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CourierService {

    private CourierRepository repository;
    private CourierMapper courierMapper;

    public CourierService(
            CourierRepository repository,
            CourierMapper courierMapper
    ) {
        this.repository = repository;
        this.courierMapper = courierMapper;
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

    public List<CourierResponseDto> getAllCouriers() {
        return courierMapper.toDto(repository.findAll());
    }
}
