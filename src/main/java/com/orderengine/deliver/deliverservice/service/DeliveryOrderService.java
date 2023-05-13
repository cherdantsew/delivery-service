package com.orderengine.deliver.deliverservice.service;

import com.orderengine.deliver.deliverservice.model.dto.DeliveryOrderRequestDto;
import com.orderengine.deliver.deliverservice.model.entity.DeliveryOrder;
import com.orderengine.deliver.deliverservice.repository.DeliveryOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryOrderService {

    private final DeliveryOrderRepository repository;
    private final CourierAssignService courierAssignService;

    public DeliveryOrderService(
            DeliveryOrderRepository repository,
            CourierAssignService courierAssignService
    ) {
        this.repository = repository;
        this.courierAssignService = courierAssignService;
    }

    public void createDeliveryOrder(DeliveryOrderRequestDto requestDto) {
        repository.save(DeliveryOrder.fromRequestDto(requestDto));
    }

    public List<DeliveryOrder> findAllOrdersWaitingForCourier() {
        return repository.findAllByCourierIdIsNull();
    }

    public void save(DeliveryOrder deliveryOrder) {
        repository.save(deliveryOrder);
    }
}
