package com.orderengine.deliver.deliverservice.controller.admin;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController("/admin/order-service/delivery-order")
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

    @PutMapping("/change-status")
    public DeliveryOrderResponseDto changeOrderStatus(@RequestBody ChangeOrderStatusRequestDto requestDto) {
        if (!SecurityUtils.isCurrentUserInPermission(AuthoritiesConstants.CHANGE_DELIVERY_STATUS)) {
            throw new ForbiddenException("No permission to view all orders");
        }
        return deliveryOrderService.changeOrderStatus(requestDto);
    }

    @GetMapping("/get-all")
    public List<DeliveryOrderResponseDto> getAllDeliveryOrders() {
        return mapper.toDto(deliveryOrderService.findAll());
    }

    @PutMapping("/assign-courier")
    public DeliveryOrderResponseDto assignCourier(@RequestBody AssignCourierToOrderRequestDto requestDto) {
        return courierAssignService.assignCourierToOrder(requestDto);
    }
}
