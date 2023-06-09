package com.deliverengine.deliver.controller.courier;

import com.deliverengine.core.enumeration.AuthoritiesConstants;
import com.deliverengine.core.exception.http.ForbiddenException;
import com.deliverengine.core.utils.SecurityUtils;
import com.deliverengine.deliver.controller.AbstractDeliveryOrderController;
import com.deliverengine.deliver.mapper.DeliveryOrderMapper;
import com.deliverengine.deliver.model.dto.ChangeOrderStatusRequestDto;
import com.deliverengine.deliver.model.dto.DeliveryOrderResponseDto;
import com.deliverengine.deliver.service.CourierDeliveryOrderService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/courier/deliver-service/delivery-orders")
public class CourierDeliveryOrderController extends AbstractDeliveryOrderController {

    private final CourierDeliveryOrderService deliveryOrderService;

    public CourierDeliveryOrderController(
        CourierDeliveryOrderService deliveryOrderService, DeliveryOrderMapper mapper
    ) {
        super(deliveryOrderService, mapper);
        this.deliveryOrderService = deliveryOrderService;
    }

    @Override
    public List<DeliveryOrderResponseDto> getAllDeliveryOrders() {
        return deliveryOrderService.findAllByCourierLogin(SecurityUtils.currentUserLoginOrException());
    }

    @Override
    @GetMapping("/{id}")
    public DeliveryOrderResponseDto getById(@PathVariable Long id) {
        return deliveryOrderService.findByIdAndCourierLogin(id, SecurityUtils.currentUserLoginOrException());
    }

    @PutMapping("/{id}/change-status")
    public DeliveryOrderResponseDto changeOrderStatus(@RequestBody ChangeOrderStatusRequestDto requestDto, @PathVariable Long id) {
        if (!SecurityUtils.isCurrentUserInPermission(AuthoritiesConstants.CHANGE_DELIVERY_STATUS)) {
            throw new ForbiddenException("No permission to change delivery status");
        }
        return deliveryOrderService.changeOrderStatus(requestDto, id);
    }
}
