package com.deliverengine.deliver.controller.admin;

import com.deliverengine.deliver.controller.AbstractDeliveryOrderController;
import com.deliverengine.deliver.mapper.DeliveryOrderMapper;
import com.deliverengine.deliver.model.dto.AssignCourierToOrderRequestDto;
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

    public AdminDeliveryOrderController(
        AdminDeliveryOrderService deliveryOrderService,
        DeliveryOrderMapper mapper,
        CourierAssignService courierAssignService
    ) {
        super(deliveryOrderService, mapper);
        this.courierAssignService = courierAssignService;
    }

    @PutMapping("/{id}/assign-courier")
    public DeliveryOrderResponseDto assignCourier(@PathVariable Long id, @RequestBody AssignCourierToOrderRequestDto requestDto) {
        return courierAssignService.assignCourierToOrder(id, requestDto);
    }

}
