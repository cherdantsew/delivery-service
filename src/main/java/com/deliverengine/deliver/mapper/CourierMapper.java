package com.deliverengine.deliver.mapper;

import com.deliverengine.deliver.model.dto.CourierResponseDto;
import com.deliverengine.deliver.model.entity.Courier;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourierMapper {
    List<CourierResponseDto> toDto(List<Courier> courier);
}
