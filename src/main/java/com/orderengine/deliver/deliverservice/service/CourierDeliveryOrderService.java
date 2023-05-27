package com.orderengine.deliver.deliverservice.service;

import com.orderengine.deliver.deliverservice.mapper.DeliveryOrderMapper;
import com.orderengine.deliver.deliverservice.model.dto.ChangeOrderStatusRequestDto;
import com.orderengine.deliver.deliverservice.model.dto.DeliveryOrderResponseDto;
import com.orderengine.deliver.deliverservice.model.entity.DeliveryOrder;
import com.orderengine.deliver.deliverservice.model.enumeration.OrderStatus;
import com.orderengine.deliver.deliverservice.repository.DeliveryOrderRepository;
import com.orderengine.deliver.deliverservice.utils.SecurityUtils;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CourierDeliveryOrderService extends AbstractDeliveryOrderService {

    public CourierDeliveryOrderService(
            DeliveryOrderRepository repository,
            UserService userService,
            DeliveryOrderMapper mapper
    ) {
        super(repository, userService, mapper);
    }

    public List<DeliveryOrderResponseDto> findAllByCourierLogin(String courierLogin) {
        return mapper.toDto(
                repository.findAllByCourierLoginAndOrderStatusNotIn(
                        courierLogin,
                        List.of(OrderStatus.DELIVERED, OrderStatus.CANCELLED)
                )
        );
    }

    public DeliveryOrderResponseDto changeOrderStatus(ChangeOrderStatusRequestDto requestDto, Long id) {
        DeliveryOrder deliveryOrder = repository.findByIdAndCourierLogin(id, SecurityUtils.currentUserLoginOrException()).orElseThrow();
        deliveryOrder.setOrderStatus(requestDto.getOrderStatus());
        return mapper.toDto(repository.saveAndFlush(deliveryOrder));
    }

    public DeliveryOrderResponseDto findByIdAndCourierLogin(Long orderId, String courierLogin) {
        return mapper.toDto(repository.findByIdAndCourierLogin(orderId, courierLogin).orElseThrow());
    }

    public List<DeliveryOrder> findAllOrdersWaitingForCourier() {
        return repository.findAllByCourierIdIsNull();
    }
}
