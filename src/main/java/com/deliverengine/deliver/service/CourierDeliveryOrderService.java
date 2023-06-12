package com.deliverengine.deliver.service;

import com.deliverengine.core.utils.SecurityUtils;
import com.deliverengine.deliver.mapper.DeliveryOrderMapper;
import com.deliverengine.deliver.model.dto.ChangeOrderStatusRequestDto;
import com.deliverengine.deliver.model.dto.DeliveryOrderResponseDto;
import com.deliverengine.deliver.model.entity.DeliveryOrder;
import com.deliverengine.deliver.model.enumeration.OrderStatus;
import com.deliverengine.deliver.repository.DeliveryOrderRepository;
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
