package com.deliverengine.deliver.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "waiting_for_courier_assign_orders")
public class WaitingForCourierAssignOrder {
    @Id
    @Column(name = "order_id")
    private Long orderId;
    @Column(name = "is_assigned")
    private Boolean isAssigned;
}
