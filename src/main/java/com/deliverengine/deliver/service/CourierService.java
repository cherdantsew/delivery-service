package com.deliverengine.deliver.service;

import com.deliverengine.deliver.model.enumeration.CourierStatus;
import com.deliverengine.deliver.repository.CourierRepository;
import com.deliverengine.deliver.service.abstraction.IBaseEntityService;
import com.deliverengine.deliver.mapper.CourierMapper;
import com.deliverengine.deliver.model.entity.Courier;
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
