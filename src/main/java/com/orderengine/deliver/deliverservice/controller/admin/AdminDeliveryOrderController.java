package com.orderengine.deliver.deliverservice.controller.admin;

import com.orderengine.deliver.deliverservice.controller.AbstractDeliveryOrderController;
import com.orderengine.deliver.deliverservice.exception.http.ForbiddenException;
import com.orderengine.deliver.deliverservice.mapper.DeliveryOrderMapper;
import com.orderengine.deliver.deliverservice.model.dto.AssignCourierToOrderRequestDto;
import com.orderengine.deliver.deliverservice.model.dto.ChangeOrderStatusRequestDto;
import com.orderengine.deliver.deliverservice.model.dto.DeliveryOrderResponseDto;
import com.orderengine.deliver.deliverservice.model.enumeration.AuthoritiesConstants;
import com.orderengine.deliver.deliverservice.service.AdminDeliveryOrderService;
import com.orderengine.deliver.deliverservice.service.CourierAssignService;
import com.orderengine.deliver.deliverservice.utils.SecurityUtils;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/admin/order-service/delivery-orders")
public class AdminDeliveryOrderController extends AbstractDeliveryOrderController {

    private final AdminDeliveryOrderService deliveryOrderService;
    private final CourierAssignService courierAssignService;
    private final DeliveryOrderMapper mapper;

    public AdminDeliveryOrderController(
        AdminDeliveryOrderService deliveryOrderService,
        DeliveryOrderMapper mapper,
        CourierAssignService courierAssignService, DeliveryOrderMapper mapper1) {
        super(deliveryOrderService, mapper);
        this.deliveryOrderService = deliveryOrderService;
        this.courierAssignService = courierAssignService;
        this.mapper = mapper1;
    }

    @PutMapping("/{id}/change-status")
    public DeliveryOrderResponseDto changeOrderStatus(@RequestBody ChangeOrderStatusRequestDto requestDto, @PathVariable Long id) {
        if (!SecurityUtils.isCurrentUserInPermission(AuthoritiesConstants.CHANGE_DELIVERY_STATUS)) {
            throw new ForbiddenException("No permission to view all orders");
        }
        return deliveryOrderService.changeOrderStatus(requestDto, id);
    }

    @PutMapping("/{id}/assign-courier")
    public DeliveryOrderResponseDto assignCourier(@PathVariable Long id, @RequestBody AssignCourierToOrderRequestDto requestDto) {
        return courierAssignService.assignCourierToOrder(id, requestDto);
    }
}
