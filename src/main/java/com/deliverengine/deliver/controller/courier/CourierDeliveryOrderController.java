package com.deliverengine.deliver.controller.courier;

import com.deliverengine.core.utils.SecurityUtils;
import com.deliverengine.deliver.controller.AbstractDeliveryOrderController;
import com.deliverengine.deliver.mapper.DeliveryOrderMapper;
import com.deliverengine.deliver.model.dto.DeliveryOrderResponseDto;
import com.deliverengine.deliver.service.CourierDeliveryOrderService;
import java.util.List;
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
    public DeliveryOrderResponseDto getById(Long id) {
        return deliveryOrderService.findByIdAndCourierLogin(id, SecurityUtils.currentUserLoginOrException());
    }

}
