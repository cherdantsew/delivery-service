package com.deliverengine.deliver.helper;

import com.deliverengine.deliver.model.entity.Courier;
import com.deliverengine.deliver.model.enumeration.CourierStatus;
import java.util.stream.LongStream;

import static kafka.utils.TestUtils.randomString;

public class CourierHelper {
    private CourierHelper(){};

    public static Courier createCourier(CourierStatus courierStatus) {
        return Courier.builder()
            .id(LongStream.range(1, 3000).findFirst().getAsLong())
            .login(randomString(10))
            .courierStatus(courierStatus)
            .build();
    }
}
