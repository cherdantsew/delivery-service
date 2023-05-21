package com.orderengine.deliver.deliverservice.service;

import com.orderengine.deliver.deliverservice.exception.http.BadRequestException;
import com.orderengine.deliver.deliverservice.mapper.DeliveryOrderMapper;
import com.orderengine.deliver.deliverservice.model.dto.AssignCourierToOrderRequestDto;
import com.orderengine.deliver.deliverservice.model.dto.DeliveryOrderResponseDto;
import com.orderengine.deliver.deliverservice.model.entity.Courier;
import com.orderengine.deliver.deliverservice.model.entity.DeliveryOrder;
import com.orderengine.deliver.deliverservice.model.enumeration.CourierStatus;
import com.orderengine.deliver.deliverservice.model.enumeration.OrderStatus;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourierAssignService {

    private final CourierService courierService;
    private final DeliveryOrderService deliveryOrderService;
    private final DeliveryOrderMapper deliveryOrderMapper;

    public CourierAssignService(
            CourierService courierService,
            DeliveryOrderService deliveryOrderService,
            DeliveryOrderMapper mapper
    ) {
        this.courierService = courierService;
        this.deliveryOrderService = deliveryOrderService;
        this.deliveryOrderMapper = mapper;
    }


    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void tryAssignCourierToOrders() {
        List<DeliveryOrder> unassignedOrders = deliveryOrderService.findAllOrdersWaitingForCourier();
        if (CollectionUtils.isEmpty(unassignedOrders))
            return;
        List<Courier> freeCouriers = courierService.findAllByCourierStatus(CourierStatus.FREE);
        if (CollectionUtils.isEmpty(freeCouriers))
            return;
        freeCouriers.forEach(freeCourier -> {
            if (unassignedOrders.isEmpty())
                return;
            DeliveryOrder unassignedOrder = unassignedOrders.get(0);
            unassignedOrder.setCourier(freeCourier);
            unassignedOrder.setOrderStatus(OrderStatus.COURIER_ASSIGNED);
            freeCourier.setCourierStatus(CourierStatus.BUSY);
            deliveryOrderService.save(unassignedOrder);
            courierService.save(freeCourier);
            unassignedOrders.remove(unassignedOrder);
        });
    }

    @Transactional
    public DeliveryOrderResponseDto assignCourierToOrder(AssignCourierToOrderRequestDto requestDto) {
        Courier courier = courierService.findOneById(requestDto.getCourierId());
        if (CourierStatus.BUSY == courier.getCourierStatus()) {
            throw new BadRequestException("You cant assign order to courier as he is delivering an order.");
        }
        DeliveryOrder deliveryOrder = deliveryOrderService.findById(requestDto.getOrderId());
        if (deliveryOrder.getCourier() != null && CourierStatus.BUSY == deliveryOrder.getCourier().getCourierStatus()) {
            throw new BadRequestException("You cant assign order to courier as order has a courier assigned who delivers order.");

        }
        deliveryOrder.setCourier(courier);
        courier.setCourierStatus(CourierStatus.BUSY);
        deliveryOrderService.save(deliveryOrder);
        courierService.save(courier);
        return deliveryOrderMapper.toDto(deliveryOrder);
    }
}
