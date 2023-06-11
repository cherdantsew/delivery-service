package com.deliverengine.deliver.controller.admin;

import com.deliverengine.core.enumeration.AuthoritiesConstants;
import com.deliverengine.core.exception.http.ForbiddenException;
import com.deliverengine.core.utils.SecurityUtils;
import com.deliverengine.deliver.controller.AbstractDeliveryOrderController;
import com.deliverengine.deliver.mapper.DeliveryOrderMapper;
import com.deliverengine.deliver.model.dto.AssignCourierToOrderRequestDto;
import com.deliverengine.deliver.model.dto.ChangeOrderStatusRequestDto;
import com.deliverengine.deliver.model.dto.DeliveryOrderResponseDto;
import com.deliverengine.deliver.service.AdminDeliveryOrderService;
import com.deliverengine.deliver.service.CourierAssignService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/admin/deliver-service/delivery-orders")
public class AdminDeliveryOrderController extends AbstractDeliveryOrderController {

    private final CourierAssignService courierAssignService;
    private final AdminDeliveryOrderService adminDeliveryOrderService;

    public AdminDeliveryOrderController(
        AdminDeliveryOrderService deliveryOrderService,
        DeliveryOrderMapper mapper,
        CourierAssignService courierAssignService
    ) {
        super(deliveryOrderService, mapper);
        this.courierAssignService = courierAssignService;
        this.adminDeliveryOrderService = deliveryOrderService;
    }

    @PutMapping("/{id}/assign-courier")
    public DeliveryOrderResponseDto assignCourier(@PathVariable Long id, @RequestBody AssignCourierToOrderRequestDto requestDto) {
        return courierAssignService.assignCourierToOrder(id, requestDto);
    }

    @PutMapping("/{id}/change-status")
    public DeliveryOrderResponseDto changeOrderStatus(@RequestBody ChangeOrderStatusRequestDto requestDto, @PathVariable Long id) {
        if (!SecurityUtils.isCurrentUserInPermission(AuthoritiesConstants.CHANGE_DELIVERY_STATUS)) {
            throw new ForbiddenException("No permission to change delivery status");
        }
        return adminDeliveryOrderService.changeOrderStatus(requestDto, id);
    }
}
