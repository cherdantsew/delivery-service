package com.deliverengine.deliver.model.dto;

import com.deliverengine.deliver.model.enumeration.CourierStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourierResponseDto {
    private Long id;
    private String login;
    private CourierStatus courierStatus;
}
