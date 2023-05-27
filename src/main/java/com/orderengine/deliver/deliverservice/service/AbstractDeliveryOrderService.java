package com.orderengine.deliver.deliverservice.service;

import com.orderengine.deliver.deliverservice.exception.http.ForbiddenException;
import com.orderengine.deliver.deliverservice.mapper.DeliveryOrderMapper;
import com.orderengine.deliver.deliverservice.model.dto.ChangeOrderStatusRequestDto;
import com.orderengine.deliver.deliverservice.model.dto.DeliveryOrderRequestDto;
import com.orderengine.deliver.deliverservice.model.dto.DeliveryOrderResponseDto;
import com.orderengine.deliver.deliverservice.model.entity.DeliveryOrder;
import com.orderengine.deliver.deliverservice.model.enumeration.AuthoritiesConstants;
import com.orderengine.deliver.deliverservice.model.enumeration.RolesConstants;
import com.orderengine.deliver.deliverservice.repository.DeliveryOrderRepository;
import com.orderengine.deliver.deliverservice.service.abstraction.IBaseEntityService;
import com.orderengine.deliver.deliverservice.utils.SecurityUtils;
import java.util.List;
import java.util.Objects;

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

    public void createDeliveryOrder(DeliveryOrderRequestDto requestDto) {
        validateOrderCreation(requestDto);
        repository.save(DeliveryOrder.fromRequestDto(requestDto));
    }

    protected void validateOrderCreation(DeliveryOrderRequestDto requestDto) {
        if (!Objects.equals(requestDto.getUserLogin(), SecurityUtils.currentUserLoginOrException()) || RolesConstants.ROLE_ADMIN != SecurityUtils.currentRole())
            throw new ForbiddenException("You can only create delivery orders to yourself.");
        if (!SecurityUtils.isCurrentUserInPermission(AuthoritiesConstants.CREATE_DELIVERY_ORDER))
            throw new ForbiddenException("No permission to create delivery orders.");
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
