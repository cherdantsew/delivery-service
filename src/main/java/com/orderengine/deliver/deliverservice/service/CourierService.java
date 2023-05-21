package com.orderengine.deliver.deliverservice.service;

import com.orderengine.deliver.deliverservice.mapper.CourierMapper;
import com.orderengine.deliver.deliverservice.model.dto.CourierResponseDto;
import com.orderengine.deliver.deliverservice.model.entity.Courier;
import com.orderengine.deliver.deliverservice.model.enumeration.CourierStatus;
import com.orderengine.deliver.deliverservice.repository.CourierRepository;
import com.orderengine.deliver.deliverservice.service.abstraction.IBaseEntityService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CourierService implements IBaseEntityService<Courier> {

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

    @Override
    public Courier findOneById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public List<Courier> findAll() {
        return repository.findAll();
    }

    @Override
    public Courier saveAndFlush(Courier entity) {
        return repository.saveAndFlush(entity);
    }

    @Override
    public void save(Courier entity) {
        repository.save(entity);
    }
}
