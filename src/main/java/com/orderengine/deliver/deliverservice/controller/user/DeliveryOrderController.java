package com.orderengine.deliver.deliverservice.controller.user;

import com.orderengine.deliver.deliverservice.exception.http.BadRequestException;
import com.orderengine.deliver.deliverservice.exception.http.ForbiddenException;
import com.orderengine.deliver.deliverservice.model.dto.ChangeDeliveryDestinationRequestDto;
import com.orderengine.deliver.deliverservice.model.dto.DeliveryOrderRequestDto;
import com.orderengine.deliver.deliverservice.model.dto.DeliveryOrderResponseDto;
import com.orderengine.deliver.deliverservice.model.enumeration.AuthoritiesConstants;
import com.orderengine.deliver.deliverservice.service.DeliveryOrderService;
import com.orderengine.deliver.deliverservice.utils.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Validated
@RestController("/user/order-service/delivery-order")
public class DeliveryOrderController {

    private DeliveryOrderService deliveryOrderService;

    public DeliveryOrderController(DeliveryOrderService deliveryOrderService) {
        this.deliveryOrderService = deliveryOrderService;
    }

    @GetMapping("/{orderId}")
    public DeliveryOrderResponseDto getDeliveryOrder(@PathVariable Long orderId) {
        return deliveryOrderService.getDeliveryOrderById(orderId, SecurityUtils.currentUserLoginOrException());
    }

    @PutMapping("/{orderId}/cancel")
    public DeliveryOrderResponseDto cancelDeliveryOrder(@PathVariable Long orderId) {
        return deliveryOrderService.cancelDeliveryOrder(orderId, SecurityUtils.currentUserLoginOrException());
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

    @PutMapping("/change-order-destination")
    public void changeOrderDestination(@RequestBody @Valid ChangeDeliveryDestinationRequestDto requestDto) {
        if (!SecurityUtils.isCurrentUserInPermission(AuthoritiesConstants.CHANGE_DELIVERY_DESTINATION)) {
            throw new BadRequestException("You have no permission to change delivery destination");
        }
        deliveryOrderService.changeOrderDestination(requestDto);
    }
}
