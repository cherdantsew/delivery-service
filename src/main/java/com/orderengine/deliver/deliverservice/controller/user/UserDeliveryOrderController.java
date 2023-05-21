package com.orderengine.deliver.deliverservice.controller.user;

import com.orderengine.deliver.deliverservice.exception.http.BadRequestException;
import com.orderengine.deliver.deliverservice.exception.http.ForbiddenException;
import com.orderengine.deliver.deliverservice.model.dto.ChangeDeliveryDestinationRequestDto;
import com.orderengine.deliver.deliverservice.model.dto.DeliveryOrderDetailsResponseDto;
import com.orderengine.deliver.deliverservice.model.dto.DeliveryOrderRequestDto;
import com.orderengine.deliver.deliverservice.model.dto.DeliveryOrderResponseDto;
import com.orderengine.deliver.deliverservice.model.enumeration.AuthoritiesConstants;
import com.orderengine.deliver.deliverservice.service.DeliveryOrderService;
import com.orderengine.deliver.deliverservice.utils.SecurityUtils;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController("/user/order-service/delivery-order")
public class UserDeliveryOrderController {

    private final DeliveryOrderService deliveryOrderService;

    public UserDeliveryOrderController(DeliveryOrderService deliveryOrderService) {
        this.deliveryOrderService = deliveryOrderService;
    }

    @GetMapping("/{orderId}")
    public DeliveryOrderDetailsResponseDto getDeliveryOrder(@PathVariable Long orderId) {
        return deliveryOrderService.getDeliveryOrderByIdAndUserLogin(orderId, SecurityUtils.currentUserLoginOrException());
    }

    @GetMapping("/get-all")
    public List<DeliveryOrderResponseDto> getAllDeliveryOrders() {
        return deliveryOrderService.findAllByUserLogin(SecurityUtils.currentUserLoginOrException());
    }

    @PutMapping("/{orderId}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelDeliveryOrder(@PathVariable Long orderId) {
        deliveryOrderService.cancelDeliveryOrder(orderId, SecurityUtils.currentUserLoginOrException(), SecurityUtils.currentRole());
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
