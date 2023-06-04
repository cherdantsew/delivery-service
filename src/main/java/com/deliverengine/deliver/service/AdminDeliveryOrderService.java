package com.deliverengine.deliver.service;

import com.deliverengine.deliver.repository.DeliveryOrderRepository;
import com.deliverengine.deliver.mapper.DeliveryOrderMapper;
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
