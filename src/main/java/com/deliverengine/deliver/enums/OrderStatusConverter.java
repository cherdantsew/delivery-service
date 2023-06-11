package com.deliverengine.deliver.enums;

import com.deliverengine.deliver.model.enumeration.OrderStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class OrderStatusConverter implements AttributeConverter<OrderStatus, String> {
    @Override
    public String convertToDatabaseColumn(OrderStatus orderStatus) {
        return orderStatus == null ? null : orderStatus.name();
    }

    @Override
    public OrderStatus convertToEntityAttribute(String s) {
        return OrderStatus.valueOf(s);
    }
}
