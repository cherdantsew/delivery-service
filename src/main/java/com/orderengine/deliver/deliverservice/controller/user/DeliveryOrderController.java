package com.orderengine.deliver.deliverservice.controller.user;

import com.orderengine.deliver.deliverservice.http.ForbiddenException;
import com.orderengine.deliver.deliverservice.model.dto.DeliveryOrderRequestDto;
import com.orderengine.deliver.deliverservice.model.enumeration.AuthoritiesConstants;
import com.orderengine.deliver.deliverservice.service.DeliveryOrderService;
import com.orderengine.deliver.deliverservice.utils.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController("/user/order-service/delivery-order")
public class DeliveryOrderController {

    private DeliveryOrderService deliveryOrderService;

    public DeliveryOrderController(DeliveryOrderService deliveryOrderService) {
        this.deliveryOrderService = deliveryOrderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createDeliveryOrder(@RequestBody DeliveryOrderRequestDto requestDto) {
        if (!Objects.equals(requestDto.getUserLogin(), SecurityUtils.currentUserLoginOrException()))
            throw new ForbiddenException("You can only create delivery orders to yourself.");
        if (!SecurityUtils.isCurrentUserInPermission(AuthoritiesConstants.CREATE_DELIVERY_ORDER))
            throw new ForbiddenException("No permission to create delivery orders.");
        deliveryOrderService.createDeliveryOrder(requestDto);
    }
}
