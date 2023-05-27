package com.orderengine.deliver.deliverservice.controller.user;

import com.orderengine.deliver.deliverservice.controller.AbstractDeliveryOrderController;
import com.orderengine.deliver.deliverservice.mapper.DeliveryOrderMapper;
import com.orderengine.deliver.deliverservice.model.dto.ChangeDeliveryDestinationRequestDto;
import com.orderengine.deliver.deliverservice.model.dto.DeliveryOrderDetailsResponseDto;
import com.orderengine.deliver.deliverservice.model.dto.DeliveryOrderRequestDto;
import com.orderengine.deliver.deliverservice.model.dto.DeliveryOrderResponseDto;
import com.orderengine.deliver.deliverservice.service.UserDeliveryOrderService;
import com.orderengine.deliver.deliverservice.utils.SecurityUtils;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/user/order-service/delivery-orders")
public class UserDeliveryOrderController extends AbstractDeliveryOrderController {

    private final UserDeliveryOrderService deliveryOrderService;

    public UserDeliveryOrderController(
        UserDeliveryOrderService deliveryOrderService,
        DeliveryOrderMapper mapper
    ) {
        super(deliveryOrderService, mapper);
        this.deliveryOrderService = deliveryOrderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createDeliveryOrder(@RequestBody DeliveryOrderRequestDto requestDto) {
        deliveryOrderService.createDeliveryOrder(requestDto);
    }

    @PutMapping("/{id}/change-order-destination")
    public void changeOrderDestination(@PathVariable Long id, @RequestBody @Valid ChangeDeliveryDestinationRequestDto requestDto) {
        deliveryOrderService.changeOrderDestination(id, requestDto);
    }

    @PutMapping("/{id}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelDeliveryOrder(@PathVariable Long id) {
        deliveryOrderService.cancelDeliveryOrder(id, SecurityUtils.currentUserLoginOrException(), SecurityUtils.currentRole());
    }

    @GetMapping("/{id}/details")
    public DeliveryOrderDetailsResponseDto getDeliveryOrderDetailed(@PathVariable Long id) {
        return deliveryOrderService.getOrderDetails(id, SecurityUtils.currentUserLoginOrException());
    }

    @Override
    public List<DeliveryOrderResponseDto> getAllDeliveryOrders() {
        return deliveryOrderService.findAllByUserLogin(SecurityUtils.currentUserLoginOrException());
    }
}
