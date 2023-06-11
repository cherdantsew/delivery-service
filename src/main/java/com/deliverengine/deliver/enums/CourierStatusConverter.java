package com.deliverengine.deliver.enums;

import com.deliverengine.deliver.model.enumeration.CourierStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CourierStatusConverter implements AttributeConverter<CourierStatus, String> {
    @Override
    public String convertToDatabaseColumn(CourierStatus courierStatus) {
        return courierStatus == null ? null : courierStatus.name();
    }

    @Override
    public CourierStatus convertToEntityAttribute(String s) {
        return CourierStatus.valueOf(s);
    }
}
