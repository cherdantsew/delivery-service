package com.deliverengine.deliver.helper;

import com.deliverengine.deliver.model.entity.Courier;
import com.deliverengine.deliver.model.entity.DeliveryOrder;
import com.deliverengine.deliver.model.enumeration.OrderStatus;
import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

import static org.apache.kafka.test.TestUtils.RANDOM;
import static org.apache.kafka.test.TestUtils.randomString;

public class DeliveryOrderHelper {

    private DeliveryOrderHelper(){

    }

    public static DeliveryOrder createDeliveryOrder(OrderStatus orderStatus) {
        return DeliveryOrder.builder()
            .id(RANDOM.nextLong())
            .orderStatus(orderStatus)
            .deliveryCost(BigDecimal.ONE)
            .userLogin(randomString(10))
            .destination(randomString(10))
            .build();
    }
    public static DeliveryOrder createDeliveryOrder(OrderStatus orderStatus, Courier courier) {
        return DeliveryOrder.builder()
            .id(ThreadLocalRandom.current().nextLong(1, Integer.MAX_VALUE))
            .courier(courier)
            .orderStatus(orderStatus)
            .deliveryCost(BigDecimal.ONE)
            .userLogin(randomString(10))
            .destination(randomString(10))
            .build();
    }
}
