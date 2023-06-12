package com.deliverengine.deliver.service;

import com.deliverengine.deliver.mapper.DeliveryOrderMapper;
import com.deliverengine.deliver.model.dto.ChangeOrderStatusRequestDto;
import com.deliverengine.deliver.model.dto.DeliveryOrderResponseDto;
import com.deliverengine.deliver.model.entity.DeliveryOrder;
import com.deliverengine.deliver.repository.DeliveryOrderRepository;
import com.deliverengine.deliver.service.abstraction.IBaseEntityService;
import java.util.List;

public abstract class AbstractDeliveryOrderService implements IBaseEntityService<DeliveryOrder> {

    protected final UserService userService;
    protected final DeliveryOrderRepository repository;
    protected final DeliveryOrderMapper mapper;

    public AbstractDeliveryOrderService(
            DeliveryOrderRepository repository,
            UserService userService,
            DeliveryOrderMapper mapper
    ) {
        this.repository = repository;
        this.userService = userService;
        this.mapper = mapper;
    }

    @Override
    public DeliveryOrder findOneById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public List<DeliveryOrder> findAll() {
        return repository.findAll();
    }

    public void save(DeliveryOrder deliveryOrder) {
        repository.save(deliveryOrder);
    }

    @Override
    public DeliveryOrder saveAndFlush(DeliveryOrder entity) {
        return repository.saveAndFlush(entity);
    }

    public DeliveryOrderResponseDto changeOrderStatus(ChangeOrderStatusRequestDto requestDto, Long id) {
        DeliveryOrder deliveryOrder = repository.findById(id).orElseThrow();
        deliveryOrder.setOrderStatus(requestDto.getOrderStatus());
        return mapper.toDto(repository.saveAndFlush(deliveryOrder));
    }


    public DeliveryOrder findById(Long orderId) {
        return repository.findById(orderId).orElseThrow();
    }

}
