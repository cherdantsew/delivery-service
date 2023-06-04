package com.deliverengine.deliver.model.dto;

import com.deliverengine.deliver.model.enumeration.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeOrderStatusRequestDto {
    private OrderStatus orderStatus;
}
