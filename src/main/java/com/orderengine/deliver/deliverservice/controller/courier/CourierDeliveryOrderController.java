package com.orderengine.deliver.deliverservice.controller.courier;

import com.orderengine.deliver.deliverservice.controller.AbstractDeliveryOrderController;
import com.orderengine.deliver.deliverservice.exception.http.ForbiddenException;
import com.orderengine.deliver.deliverservice.mapper.DeliveryOrderMapper;
import com.orderengine.deliver.deliverservice.model.dto.ChangeOrderStatusRequestDto;
import com.orderengine.deliver.deliverservice.model.dto.DeliveryOrderResponseDto;
import com.orderengine.deliver.deliverservice.model.enumeration.AuthoritiesConstants;
import com.orderengine.deliver.deliverservice.service.CourierDeliveryOrderService;
import com.orderengine.deliver.deliverservice.utils.SecurityUtils;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/courier/order-service/delivery-order")
public class CourierDeliveryOrderController extends AbstractDeliveryOrderController {

    private final CourierDeliveryOrderService deliveryOrderService;

    public CourierDeliveryOrderController(
            CourierDeliveryOrderService deliveryOrderService, DeliveryOrderMapper mapper
    ) {
        super(deliveryOrderService, mapper);
        this.deliveryOrderService = deliveryOrderService;
    }

    @GetMapping("/get-all")
    public List<DeliveryOrderResponseDto> getAllDeliveryOrders() {
        return deliveryOrderService.findAllByCourierLogin(SecurityUtils.currentUserLoginOrException());
    }

    @PutMapping("/change-status")
    public DeliveryOrderResponseDto changeOrderStatus(@RequestBody ChangeOrderStatusRequestDto requestDto) {
        if (!SecurityUtils.isCurrentUserInPermission(AuthoritiesConstants.CHANGE_DELIVERY_STATUS)) {
            throw new ForbiddenException("No permission to view all orders");
        }
        return deliveryOrderService.changeOrderStatus(requestDto, SecurityUtils.currentUserLoginOrException(), SecurityUtils.currentRole());
    }

    @GetMapping("/{orderId}")
    public DeliveryOrderResponseDto getDeliveryOrder(@PathVariable Long orderId) {
        return deliveryOrderService.findByIdAndCourierLogin(orderId, SecurityUtils.currentUserLoginOrException());
    }
}
