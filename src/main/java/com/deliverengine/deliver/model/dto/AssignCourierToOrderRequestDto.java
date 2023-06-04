package com.deliverengine.deliver.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignCourierToOrderRequestDto {
    private Long courierId;
}
