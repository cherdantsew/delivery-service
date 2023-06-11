package com.deliverengine.deliver.controller;

import com.deliverengine.core.enumeration.AuthoritiesConstants;
import com.deliverengine.core.exception.http.UnauthorizedException;
import com.deliverengine.core.utils.SecurityUtils;
import com.deliverengine.deliver.model.dto.CourierResponseDto;
import com.deliverengine.deliver.mapper.CourierMapper;
import com.deliverengine.deliver.service.CourierService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/deliver-service/couriers")
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
