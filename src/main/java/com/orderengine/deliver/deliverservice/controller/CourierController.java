package com.orderengine.deliver.deliverservice.controller;

import com.orderengine.deliver.deliverservice.exception.http.UnauthorizedException;
import com.orderengine.deliver.deliverservice.mapper.CourierMapper;
import com.orderengine.deliver.deliverservice.model.dto.CourierResponseDto;
import com.orderengine.deliver.deliverservice.model.enumeration.AuthoritiesConstants;
import com.orderengine.deliver.deliverservice.service.CourierService;
import com.orderengine.deliver.deliverservice.utils.SecurityUtils;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/order-service/couriers")
public class CourierController {

    private final CourierService courierService;
    private final CourierMapper courierMapper;

    public CourierController(CourierService courierService, CourierMapper courierMapper) {
        this.courierService = courierService;
        this.courierMapper = courierMapper;
    }

    @GetMapping
    public List<CourierResponseDto> getAllCouriers() {
        if (!SecurityUtils.isCurrentUserInPermission(AuthoritiesConstants.VIEW_ALL_COURIERS)) {
            throw new UnauthorizedException("You have no permission to view all couriers");
        }
        return courierMapper.toDto(courierService.findAll());
    }
}
