package com.orderengine.deliver.deliverservice.enums;

import com.orderengine.deliver.deliverservice.model.enumeration.RolesConstants;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RolesConstantsConverter implements AttributeConverter<RolesConstants, String> {
    @Override
    public String convertToDatabaseColumn(RolesConstants rolesConstants) {
        return rolesConstants == null? null : rolesConstants.name();
    }

    @Override
    public RolesConstants convertToEntityAttribute(String s) {
        return RolesConstants.valueOf(s);
    }
}
