package com.orderengine.deliver.deliverservice.controller;

import com.orderengine.deliver.deliverservice.mapper.DeliveryOrderMapper;
import com.orderengine.deliver.deliverservice.model.dto.DeliveryOrderResponseDto;
import com.orderengine.deliver.deliverservice.service.AbstractDeliveryOrderService;
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
        return mapper.toDto(deliveryOrderService.findAll());
    }

    @GetMapping("/{id}")
    public DeliveryOrderResponseDto getById(@PathVariable Long id) {
        return mapper.toDto(deliveryOrderService.findById(id));
    }
}
