package com.orderengine.deliver.deliverservice.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeDeliveryDestinationRequestDto {
    @NotNull
    @Positive
    private Long orderId;
    @NotBlank
    private String newDestination;
}
