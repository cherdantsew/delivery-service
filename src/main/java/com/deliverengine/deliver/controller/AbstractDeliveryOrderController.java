package com.deliverengine.deliver.controller;

import com.deliverengine.core.enumeration.AuthoritiesConstants;
import com.deliverengine.core.exception.http.ForbiddenException;
import com.deliverengine.core.utils.SecurityUtils;
import com.deliverengine.deliver.mapper.DeliveryOrderMapper;
import com.deliverengine.deliver.model.dto.ChangeOrderStatusRequestDto;
import com.deliverengine.deliver.model.dto.DeliveryOrderResponseDto;
import com.deliverengine.deliver.service.AbstractDeliveryOrderService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

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


    @PutMapping("/{id}/change-status")
    public DeliveryOrderResponseDto changeOrderStatus(@RequestBody ChangeOrderStatusRequestDto requestDto, @PathVariable Long id) {
        if (!SecurityUtils.isCurrentUserInPermission(AuthoritiesConstants.CHANGE_DELIVERY_STATUS)) {
            throw new ForbiddenException("No permission to change delivery status");
        }
        return deliveryOrderService.changeOrderStatus(requestDto, id);
    }
}
