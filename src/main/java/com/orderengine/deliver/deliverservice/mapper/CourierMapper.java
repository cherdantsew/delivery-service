package com.orderengine.deliver.deliverservice.mapper;

import com.orderengine.deliver.deliverservice.model.dto.CourierResponseDto;
import com.orderengine.deliver.deliverservice.model.entity.Courier;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourierMapper {
    List<CourierResponseDto> toDto(List<Courier> courier);
}
