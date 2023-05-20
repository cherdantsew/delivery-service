package com.orderengine.deliver.deliverservice.mapper;

import com.orderengine.deliver.deliverservice.model.dto.DeliveryOrderResponseDto;
import com.orderengine.deliver.deliverservice.model.entity.DeliveryOrder;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeliveryOrderMapper {
    List<DeliveryOrderResponseDto> toDto(List<DeliveryOrder> deliveryOrders);
}
