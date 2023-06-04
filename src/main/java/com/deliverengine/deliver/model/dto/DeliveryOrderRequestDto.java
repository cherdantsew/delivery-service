package com.deliverengine.deliver.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryOrderRequestDto {
    private String userLogin;
    private String destination;
    private BigDecimal deliveryCost;
}
