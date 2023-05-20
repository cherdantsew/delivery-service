package com.orderengine.deliver.deliverservice.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.orderengine.deliver.deliverservice.model.dto.DeliveryOrderRequestDto;
import com.orderengine.deliver.deliverservice.model.enumeration.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
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
    @OneToOne
    @JoinColumn(name = "courier_id", referencedColumnName = "id")
    private Courier courier;
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
    @Column(name = "delivery_cost")
    private BigDecimal deliveryCost;

    public static DeliveryOrder fromRequestDto(DeliveryOrderRequestDto dto) {
        return DeliveryOrder.builder()
                .userLogin(dto.getUserLogin())
                .destination(dto.getDestination())
                .orderStatus(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .deliveryCost(dto.getDeliveryCost())
                .build();
    }
}