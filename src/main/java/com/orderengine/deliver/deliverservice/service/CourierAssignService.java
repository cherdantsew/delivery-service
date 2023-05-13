package com.orderengine.deliver.deliverservice.service;

import com.orderengine.deliver.deliverservice.model.entity.Courier;
import com.orderengine.deliver.deliverservice.model.entity.DeliveryOrder;
import com.orderengine.deliver.deliverservice.model.entity.WaitingForCourierAssignOrder;
import com.orderengine.deliver.deliverservice.model.enumeration.CourierStatus;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourierAssignService {

    private CourierService courierService;
    private DeliveryOrderService deliveryOrderService;

    public CourierAssignService(
            CourierService courierService,
            DeliveryOrderService deliveryOrderService
    ) {
        this.courierService = courierService;
        this.deliveryOrderService = deliveryOrderService;
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
            unassignedOrder.setCourierId(freeCourier.getId());
            freeCourier.setCourierStatus(CourierStatus.BUSY);
            deliveryOrderService.save(unassignedOrder);
            courierService.save(freeCourier);
            unassignedOrders.remove(unassignedOrder);
        });
    }
}
