package com.orderengine.deliver.deliverservice.model.dto;

import com.orderengine.deliver.deliverservice.model.enumeration.OrderStatus;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
public class DeliveryOrderDetailsResponseDto {
    private String userLogin;
    private BigDecimal deliveryCost;
    private String destination;
    private LocalDateTime createdAt;
    private OrderStatus orderStatus;
    private LocalDateTime deliveredAt;
    private String courierLogin;
    private String courierContactInfo;

    public DeliveryOrderDetailsResponseDto(String userLogin, BigDecimal deliveryCost, String destination, LocalDateTime createdAt, OrderStatus orderStatus, LocalDateTime deliveredAt, String courierLogin, String courierContactInfo) {
        this.userLogin = userLogin;
        this.deliveryCost = deliveryCost;
        this.destination = destination;
        this.createdAt = createdAt;
        this.orderStatus = orderStatus;
        this.deliveredAt = deliveredAt;
        this.courierLogin = courierLogin;
        this.courierContactInfo = courierContactInfo;
    }

}
