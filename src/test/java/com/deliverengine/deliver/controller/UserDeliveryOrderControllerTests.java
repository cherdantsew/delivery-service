package com.deliverengine.deliver.controller;

import com.deliverengine.core.enumeration.AuthoritiesConstants;
import com.deliverengine.core.enumeration.RolesConstants;
import com.deliverengine.deliver.SpringBootApplicationTest;
import com.deliverengine.deliver.model.dto.DeliveryOrderRequestDto;
import com.deliverengine.deliver.model.entity.DeliveryOrder;
import com.deliverengine.deliver.model.entity.User;
import com.deliverengine.deliver.repository.DeliveryOrderRepository;
import com.deliverengine.deliver.repository.UserRepository;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static com.deliverengine.deliver.helper.UserHelper.createUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserDeliveryOrderControllerTests extends SpringBootApplicationTest {

    private static final String USER_DELIVER_ORDER_URL = "/user/deliver-service/delivery-orders";

    @Autowired
    private DeliveryOrderRepository deliveryOrderRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("should create delivery order and substract account balance")
    @Test
    public void shouldCreateDeliveryOrder() throws Exception {
        User user = createUser(new BigDecimal(100));
        insert(user);
        DeliveryOrderRequestDto requestDto = new DeliveryOrderRequestDto(user.getLogin(), "destination", new BigDecimal(50));
        authenticateAndReturnToken(user.getLogin(), RolesConstants.ROLE_USER, List.of(AuthoritiesConstants.CREATE_DELIVERY_ORDER));
        mockmvc.perform(post(USER_DELIVER_ORDER_URL)
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());

        DeliveryOrder deliveryOrder = deliveryOrderRepository.findAll().stream().findFirst().orElseThrow();
        assertEquals(requestDto.getUserLogin(), deliveryOrder.getUserLogin());
        assertEquals(requestDto.getDeliveryCost(), deliveryOrder.getDeliveryCost());
        assertEquals(requestDto.getDestination(), deliveryOrder.getDestination());
        assertEquals(BigDecimal.valueOf(50), userRepository.findByLogin(user.getLogin()).orElseThrow().getAccountBalance());
    }
}
