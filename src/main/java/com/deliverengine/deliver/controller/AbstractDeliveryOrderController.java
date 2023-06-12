package com.deliverengine.deliver.controller;

import com.deliverengine.core.enumeration.RolesConstants;
import com.deliverengine.core.exception.http.UnauthorizedException;
import com.deliverengine.core.utils.SecurityUtils;
import com.deliverengine.deliver.mapper.DeliveryOrderMapper;
import com.deliverengine.deliver.model.dto.DeliveryOrderResponseDto;
import com.deliverengine.deliver.service.AbstractDeliveryOrderService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public class AbstractDeliveryOrderController {

    private final AbstractDeliveryOrderService deliveryOrderService;

    private final DeliveryOrderMapper mapper;

    public AbstractDeliveryOrderController(
        AbstractDeliveryOrderService deliveryOrderService,
        DeliveryOrderMapper mapper) {
        this.deliveryOrderService = deliveryOrderService;
        this.mapper = mapper;
    }

    @GetMapping
    public List<DeliveryOrderResponseDto> getAllDeliveryOrders() {
        if (RolesConstants.ROLE_ADMIN != SecurityUtils.currentRole()) {
            throw new UnauthorizedException("Only admin can see all delivery orders");
        }
        return mapper.toDto(deliveryOrderService.findAll());
    }

    @GetMapping("/{id}")
    public DeliveryOrderResponseDto getById(@PathVariable Long id) {
        return mapper.toDto(deliveryOrderService.findById(id));
    }
}
