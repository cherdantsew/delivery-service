package com.deliverengine.deliver.helper;

import com.deliverengine.deliver.model.entity.Courier;
import com.deliverengine.deliver.model.enumeration.CourierStatus;
import java.util.concurrent.ThreadLocalRandom;

import static kafka.utils.TestUtils.randomString;

public class CourierHelper {
    private CourierHelper() {
    }

    public static Courier createCourier(CourierStatus courierStatus) {
        return Courier.builder()
            .id(ThreadLocalRandom.current().nextLong(1, Integer.MAX_VALUE))
            .login(randomString(10))
            .courierStatus(courierStatus)
            .build();
    }
}
