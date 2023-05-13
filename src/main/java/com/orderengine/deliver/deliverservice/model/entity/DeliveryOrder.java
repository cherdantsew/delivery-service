package com.orderengine.deliver.deliverservice.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.orderengine.deliver.deliverservice.model.dto.DeliveryOrderRequestDto;
import com.orderengine.deliver.deliverservice.model.enumeration.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "parcel_delivery_order")
public class DeliveryOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column(name = "user_login")
    private String userLogin;
    @Column(name = "courier_id")
    private Long courierId;
    @Column(name = "destination")
    private String destination;
    @Column(name = "order_status")
    private OrderStatus orderStatus;
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonIgnore
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    public static DeliveryOrder fromRequestDto(DeliveryOrderRequestDto dto) {
        return DeliveryOrder.builder()
            .userLogin(dto.getUserLogin())
            .destination(dto.getDestination())
            .orderStatus(OrderStatus.CREATED)
            .createdAt(LocalDateTime.now())
            .build();
    }
}
