package com.orderengine.deliver.deliverservice.controller.user;

import com.orderengine.deliver.deliverservice.exception.http.ForbiddenException;
import com.orderengine.deliver.deliverservice.model.dto.AssignCourierToOrderRequestDto;
import com.orderengine.deliver.deliverservice.model.dto.ChangeOrderStatusRequestDto;
import com.orderengine.deliver.deliverservice.model.dto.DeliveryOrderResponseDto;
import com.orderengine.deliver.deliverservice.model.enumeration.AuthoritiesConstants;
import com.orderengine.deliver.deliverservice.service.CourierAssignService;
import com.orderengine.deliver.deliverservice.service.DeliveryOrderService;
import com.orderengine.deliver.deliverservice.utils.SecurityUtils;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController("/admin/order-service/delivery-order")
public class AdminDeliveryOrderController {

    private final DeliveryOrderService deliveryOrderService;

    private final CourierAssignService courierAssignService;

    public AdminDeliveryOrderController(
            DeliveryOrderService deliveryOrderService,
            CourierAssignService courierAssignService
    ) {
        this.deliveryOrderService = deliveryOrderService;
        this.courierAssignService = courierAssignService;
    }

    @PutMapping("/change-status")
    public DeliveryOrderResponseDto changeOrderStatus(@RequestBody ChangeOrderStatusRequestDto requestDto) {
        if (!SecurityUtils.isCurrentUserInPermission(AuthoritiesConstants.VIEW_ALL_DELIVERY_ORDERS)) {
            throw new ForbiddenException("No permission to view all orders");
        }
        return deliveryOrderService.changeOrderStatus(requestDto);
    }

    @GetMapping("/get-all")
    public List<DeliveryOrderResponseDto> getAllDeliveryOrders() {
        return deliveryOrderService.findAll();
    }

    @PutMapping("/assign-courier")
    public DeliveryOrderResponseDto assignCourier(@RequestBody AssignCourierToOrderRequestDto requestDto) {
        return courierAssignService.assignCourierToOrder(requestDto);
    }

    //todo Can track the delivery order by coordinates; what are the coordinates?


}
