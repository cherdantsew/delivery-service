package com.deliverengine.deliver.model.dto;

import com.deliverengine.deliver.model.enumeration.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryOrderResponseDto {
    private Long id;
    private String destination;
    private LocalDateTime createdAt;
    private LocalDateTime deliveredAt;
    private OrderStatus orderStatus;
    private BigDecimal deliveryCost;
}
