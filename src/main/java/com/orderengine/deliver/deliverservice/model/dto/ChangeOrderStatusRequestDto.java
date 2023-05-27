package com.orderengine.deliver.deliverservice.model.dto;

import com.orderengine.deliver.deliverservice.model.enumeration.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeOrderStatusRequestDto {
    private OrderStatus orderStatus;
}
