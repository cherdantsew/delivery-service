package com.orderengine.deliver.deliverservice.service;

import com.orderengine.deliver.deliverservice.mapper.DeliveryOrderMapper;
import com.orderengine.deliver.deliverservice.repository.DeliveryOrderRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminDeliveryOrderService extends AbstractDeliveryOrderService {
    public AdminDeliveryOrderService(
            DeliveryOrderRepository repository,
            UserService userService,
            DeliveryOrderMapper mapper
    ) {
        super(repository, userService, mapper);
    }
}
