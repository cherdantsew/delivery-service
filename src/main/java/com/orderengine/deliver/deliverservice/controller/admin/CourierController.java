package com.orderengine.deliver.deliverservice.controller.admin;

import com.orderengine.deliver.deliverservice.exception.http.UnauthorizedException;
import com.orderengine.deliver.deliverservice.model.dto.CourierResponseDto;
import com.orderengine.deliver.deliverservice.model.enumeration.AuthoritiesConstants;
import com.orderengine.deliver.deliverservice.service.CourierService;
import com.orderengine.deliver.deliverservice.utils.SecurityUtils;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/admin/order-service/courier")
public class CourierController {

    private final CourierService courierService;

    public CourierController(CourierService courierService) {
        this.courierService = courierService;
    }

    @GetMapping("/get-all")
    public List<CourierResponseDto> getAllCouriers(){
        if (!SecurityUtils.isCurrentUserInPermission(AuthoritiesConstants.VIEW_ALL_COURIERS)) {
            throw new UnauthorizedException("You have no permission to view all couriers");
        }
        return courierService.getAllCouriers();
    }
}
