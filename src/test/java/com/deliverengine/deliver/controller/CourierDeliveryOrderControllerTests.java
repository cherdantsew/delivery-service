package com.deliverengine.deliver.controller;

import com.deliverengine.core.enumeration.AuthoritiesConstants;
import com.deliverengine.core.enumeration.RolesConstants;
import com.deliverengine.deliver.SpringBootApplicationTest;
import com.deliverengine.deliver.model.dto.ChangeOrderStatusRequestDto;
import com.deliverengine.deliver.model.dto.DeliveryOrderResponseDto;
import com.deliverengine.deliver.model.entity.Courier;
import com.deliverengine.deliver.model.entity.DeliveryOrder;
import com.deliverengine.deliver.model.enumeration.CourierStatus;
import com.deliverengine.deliver.model.enumeration.OrderStatus;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static com.deliverengine.deliver.helper.CourierHelper.createCourier;
import static com.deliverengine.deliver.helper.DeliveryOrderHelper.createDeliveryOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CourierDeliveryOrderControllerTests extends SpringBootApplicationTest {
    private static final String COURIER_DELIVER_ORDER_URL = "/courier/deliver-service/delivery-orders";

    @Test
    public void courierShouldChangeOrderStatus() throws Exception {
        Courier courier = createCourier(CourierStatus.BUSY);
        DeliveryOrder deliveryOrder = createDeliveryOrder(OrderStatus.CREATED, courier);
        insert(courier);
        insert(deliveryOrder);
        authenticateAndReturnToken(courier.getLogin(), RolesConstants.ROLE_COURIER, List.of(AuthoritiesConstants.CHANGE_DELIVERY_STATUS));

        ChangeOrderStatusRequestDto requestDto = new ChangeOrderStatusRequestDto(OrderStatus.DELIVERED);

        MvcResult mvcResult = mockmvc.perform(put(
            COURIER_DELIVER_ORDER_URL + String.format("/%s/change-status", deliveryOrder.getId()))
            .content(objectMapper.writeValueAsString(requestDto))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk()).andReturn();
        DeliveryOrderResponseDto response = resultAsObject(mvcResult, DeliveryOrderResponseDto.class);
        assertEquals(response.getOrderStatus(), requestDto.getOrderStatus());
    }

    @Test
    @DisplayName("should return all delivery orders that belong to courier")
    public void shouldReturnDeliveryOrders() throws Exception {
        Courier courier = createCourier(CourierStatus.BUSY);
        DeliveryOrder deliveryOrder1 = createDeliveryOrder(OrderStatus.COURIER_ASSIGNED, courier);
        DeliveryOrder deliveryOrder2 = createDeliveryOrder(OrderStatus.DELIVER_IN_PROGRESS, courier);
        //should not be returned
        DeliveryOrder deliveryOrder3 = createDeliveryOrder(OrderStatus.DELIVERED, courier);

        insert(courier);
        insert(deliveryOrder1);
        insert(deliveryOrder2);
        insert(deliveryOrder3);

        authenticateAndReturnToken(courier.getLogin(), RolesConstants.ROLE_COURIER, List.of(AuthoritiesConstants.CHANGE_DELIVERY_STATUS));
        MvcResult mvcResult = mockmvc.perform(
                get(COURIER_DELIVER_ORDER_URL)
            )
            .andExpect(status().isOk())
            .andReturn();
        List<DeliveryOrderResponseDto> deliveryOrderResponseDtos = resultAsList(mvcResult, DeliveryOrderResponseDto.class);
        assertEquals(2L, deliveryOrderResponseDtos.size());

        assertEquals(1L, deliveryOrderResponseDtos.stream()
            .filter(it -> Objects.equals(it.getId(), deliveryOrder1.getId()))
            .count());

        assertEquals(1L, deliveryOrderResponseDtos.stream()
            .filter(it -> Objects.equals(it.getId(), deliveryOrder2.getId()))
            .count());
    }

    @Test
    @DisplayName("should return delivery order by id")
    public void shouldReturnOrderById() throws Exception {
        Courier courier = createCourier(CourierStatus.BUSY);
        DeliveryOrder deliveryOrder = createDeliveryOrder(OrderStatus.COURIER_ASSIGNED, courier);
        insert(courier);
        insert(deliveryOrder);

        authenticateAndReturnToken(courier.getLogin(), RolesConstants.ROLE_COURIER, List.of(AuthoritiesConstants.CHANGE_DELIVERY_STATUS));
        MvcResult mvcResult = mockmvc.perform(
                get(COURIER_DELIVER_ORDER_URL + "/" + deliveryOrder.getId())
            )
            .andExpect(status().isOk())
            .andReturn();

        DeliveryOrderResponseDto response = resultAsObject(mvcResult, DeliveryOrderResponseDto.class);
        assertEquals(deliveryOrder.getId(), response.getId());
    }

    @Test
    @DisplayName("should not return delivery order by id if order doesnt belong to courier")
    public void shouldNotReturnOrderById() throws Exception {
        Courier courier = createCourier(CourierStatus.BUSY);
        Courier courier2 = createCourier(CourierStatus.FREE);
        DeliveryOrder deliveryOrder = createDeliveryOrder(OrderStatus.COURIER_ASSIGNED, courier2);
        insert(courier);
        insert(courier2);
        insert(deliveryOrder);

        authenticateAndReturnToken(courier.getLogin(), RolesConstants.ROLE_COURIER, List.of(AuthoritiesConstants.CHANGE_DELIVERY_STATUS));
        mockmvc.perform(
                get(COURIER_DELIVER_ORDER_URL + "/" + deliveryOrder.getId())
            )
            .andExpect(status().isNotFound());
    }
}
