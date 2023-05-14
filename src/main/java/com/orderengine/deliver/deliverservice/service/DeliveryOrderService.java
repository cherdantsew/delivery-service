package com.orderengine.deliver.deliverservice.service;

import com.orderengine.deliver.deliverservice.exception.http.BadRequestException;
import com.orderengine.deliver.deliverservice.model.dto.ChangeDeliveryDestinationRequestDto;
import com.orderengine.deliver.deliverservice.model.dto.DeliveryOrderRequestDto;
import com.orderengine.deliver.deliverservice.model.dto.DeliveryOrderResponseDto;
import com.orderengine.deliver.deliverservice.model.entity.DeliveryOrder;
import com.orderengine.deliver.deliverservice.model.enumeration.OrderStatus;
import com.orderengine.deliver.deliverservice.repository.DeliveryOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryOrderService {

    private final DeliveryOrderRepository repository;

    public DeliveryOrderService(DeliveryOrderRepository repository) {
        this.repository = repository;
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

    public void changeOrderDestination(ChangeDeliveryDestinationRequestDto requestDto) {
        DeliveryOrder deliveryOrder = repository.findById(requestDto.getOrderId()).orElseThrow();
        if (deliveryOrder.getCourier() != null && OrderStatus.DELIVER_IN_PROGRESS == deliveryOrder.getOrderStatus()) {
            throw new BadRequestException("Delivery is in progress. Too late to change delivery address.");
        }
        deliveryOrder.setDestination(requestDto.getNewDestination());
        repository.save(deliveryOrder);
    }

    public DeliveryOrderResponseDto getDeliveryOrderById(Long orderId, String currentUserLogin) {
        return repository.findDeliveryOrderById(orderId, currentUserLogin).orElseThrow();
    }

    public DeliveryOrderResponseDto cancelDeliveryOrder(Long orderId, String currentUserLogin) {
        DeliveryOrder deliveryOrder = repository.findById(orderId).orElseThrow();
        if (deliveryOrder.getCourier() != null)
            deliveryOrder.setOrderStatus(OrderStatus.CANCELLED);
        repository.save(deliveryOrder);
    }
}
