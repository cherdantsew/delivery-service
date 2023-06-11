package com.deliverengine.deliver.controller;

import com.deliverengine.core.enumeration.AuthoritiesConstants;
import com.deliverengine.core.enumeration.RolesConstants;
import com.deliverengine.deliver.SpringBootApplicationTest;
import com.deliverengine.deliver.model.dto.AssignCourierToOrderRequestDto;
import com.deliverengine.deliver.model.dto.ChangeOrderStatusRequestDto;
import com.deliverengine.deliver.model.dto.DeliveryOrderResponseDto;
import com.deliverengine.deliver.model.entity.Courier;
import com.deliverengine.deliver.model.entity.DeliveryOrder;
import com.deliverengine.deliver.model.enumeration.CourierStatus;
import com.deliverengine.deliver.model.enumeration.OrderStatus;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static com.deliverengine.deliver.helper.CourierHelper.createCourier;
import static com.deliverengine.deliver.helper.DeliveryOrderHelper.createDeliveryOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminDeliveryOrderControllerTests extends SpringBootApplicationTest {
    private static final String ADMIN_DELIVER_ORDER_URL = "/admin/deliver-service/delivery-orders";

    @Test
    @DisplayName("should change order status")
    public void shouldChangeOrderStatus() throws Exception {
        DeliveryOrder deliveryOrder = createDeliveryOrder(OrderStatus.CREATED);
        insert(deliveryOrder);
        authenticateAndReturnToken(RolesConstants.ROLE_ADMIN, List.of(AuthoritiesConstants.CHANGE_DELIVERY_STATUS));

        ChangeOrderStatusRequestDto requestDto = new ChangeOrderStatusRequestDto(OrderStatus.DELIVERED);

        MvcResult mvcResult = mockmvc.perform(put(ADMIN_DELIVER_ORDER_URL + String.format("/%s/change-status", deliveryOrder.getId()))
            .content(objectMapper.writeValueAsString(requestDto))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk()).andReturn();
        DeliveryOrderResponseDto response = resultAsObject(mvcResult, DeliveryOrderResponseDto.class);
        assertEquals(response.getOrderStatus(), requestDto.getOrderStatus());
    }

    @Test
    @DisplayName("should assign courier to order")
    public void shouldAssignCourier() throws Exception {
        DeliveryOrder deliveryOrder = createDeliveryOrder(OrderStatus.CREATED);
        Courier courier = createCourier(CourierStatus.FREE);
        insert(deliveryOrder);
        insert(courier);
        AssignCourierToOrderRequestDto requestDto = new AssignCourierToOrderRequestDto(courier.getId());
        mockmvc.perform(
                put(ADMIN_DELIVER_ORDER_URL + String.format("/%s/assign-courier", deliveryOrder.getId()))
                    .content(objectMapper.writeValueAsString(requestDto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());
        Long updatedCourierId = jdbcTemplate.queryForObject(String.format("select courier_id from parcel_delivery_order where id = %s", deliveryOrder.getId()), Long.class);
        assertEquals(updatedCourierId, courier.getId());
    }

}
