package com.deliverengine.deliver.mapper;

import com.deliverengine.deliver.model.dto.DeliveryOrderResponseDto;
import com.deliverengine.deliver.model.entity.DeliveryOrder;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeliveryOrderMapper {
    List<DeliveryOrderResponseDto> toDto(List<DeliveryOrder> deliveryOrders);
    DeliveryOrderResponseDto toDto(DeliveryOrder deliveryOrder);
}
