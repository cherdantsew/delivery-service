package com.deliverengine.deliver.helper;

import com.deliverengine.deliver.model.entity.DeliveryOrder;
import com.deliverengine.deliver.model.enumeration.OrderStatus;
import java.math.BigDecimal;
import java.util.stream.LongStream;

import static org.apache.kafka.test.TestUtils.randomString;

public class DeliveryOrderHelper {

    private DeliveryOrderHelper(){

    }

    public static DeliveryOrder createDeliveryOrder(OrderStatus orderStatus) {
        return DeliveryOrder.builder()
            .id(LongStream.range(1, 3000).findFirst().getAsLong())
            .orderStatus(orderStatus)
            .deliveryCost(BigDecimal.ONE)
            .userLogin(randomString(10))
            .destination(randomString(10))
            .build();
    }
}
