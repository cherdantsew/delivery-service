package com.deliverengine.deliver.controller;

import com.deliverengine.core.enumeration.AuthoritiesConstants;
import com.deliverengine.core.enumeration.RolesConstants;
import com.deliverengine.deliver.SpringBootApplicationTest;
import com.deliverengine.deliver.model.dto.ChangeDeliveryDestinationRequestDto;
import com.deliverengine.deliver.model.dto.DeliveryOrderDetailsResponseDto;
import com.deliverengine.deliver.model.dto.DeliveryOrderRequestDto;
import com.deliverengine.deliver.model.dto.DeliveryOrderResponseDto;
import com.deliverengine.deliver.model.entity.Courier;
import com.deliverengine.deliver.model.entity.DeliveryOrder;
import com.deliverengine.deliver.model.entity.User;
import com.deliverengine.deliver.model.enumeration.CourierStatus;
import com.deliverengine.deliver.model.enumeration.OrderStatus;
import com.deliverengine.deliver.repository.DeliveryOrderRepository;
import com.deliverengine.deliver.repository.UserRepository;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static com.deliverengine.deliver.helper.CourierHelper.createCourier;
import static com.deliverengine.deliver.helper.DeliveryOrderHelper.createDeliveryOrder;
import static com.deliverengine.deliver.helper.UserHelper.createUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.utility.Base58.randomString;

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

    @DisplayName("should change order destination")
    @Test
    public void changeOrderDestination() throws Exception {
        User user = createUser(new BigDecimal(100));
        DeliveryOrder deliveryOrder = createDeliveryOrder(OrderStatus.CREATED);
        deliveryOrder.setUserLogin(user.getLogin());
        insert(user);
        insert(deliveryOrder);
        authenticateAndReturnToken(user.getLogin(), RolesConstants.ROLE_USER, List.of(AuthoritiesConstants.CHANGE_DELIVERY_DESTINATION));
        ChangeDeliveryDestinationRequestDto requestDto = new ChangeDeliveryDestinationRequestDto("grandma");
        mockmvc.perform(put(USER_DELIVER_ORDER_URL + String.format("/%s/change-order-destination", deliveryOrder.getId()))
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
        assertEquals(requestDto.getNewDestination(), deliveryOrderRepository.findById(deliveryOrder.getId()).get().getDestination());
    }

    @DisplayName("should not let change order destination if courier already delivers it")
    @Test
    public void shouldNotChangeOrderDestination() throws Exception {
        User user = createUser(new BigDecimal(100));
        Courier courier = createCourier(CourierStatus.BUSY);
        DeliveryOrder deliveryOrder = createDeliveryOrder(OrderStatus.DELIVER_IN_PROGRESS);
        deliveryOrder.setUserLogin(user.getLogin());
        deliveryOrder.setCourier(courier);
        insert(user);
        insert(courier);
        insert(deliveryOrder);
        authenticateAndReturnToken(user.getLogin(), RolesConstants.ROLE_USER, List.of(AuthoritiesConstants.CHANGE_DELIVERY_DESTINATION));
        ChangeDeliveryDestinationRequestDto requestDto = new ChangeDeliveryDestinationRequestDto("grandma");
        mockmvc.perform(put(USER_DELIVER_ORDER_URL + String.format("/%s/change-order-destination", deliveryOrder.getId()))
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @DisplayName("should cancel delivery order and return money back to user account")
    @Test
    public void shouldCorrectlyCancelOrder() throws Exception {
        User user = createUser(new BigDecimal(100));
        Courier courier = createCourier(CourierStatus.BUSY);
        DeliveryOrder deliveryOrder = createDeliveryOrder(OrderStatus.CREATED);
        deliveryOrder.setUserLogin(user.getLogin());
        deliveryOrder.setCourier(courier);

        insert(user);
        insert(courier);
        insert(deliveryOrder);

        authenticateAndReturnToken(user.getLogin(), RolesConstants.ROLE_USER, List.of(AuthoritiesConstants.CANCEL_DELIVERY_ORDER));

        mockmvc.perform(put(USER_DELIVER_ORDER_URL + String.format("/%s/cancel", deliveryOrder.getId())))
            .andExpect(status().isOk());

        assertEquals(OrderStatus.CANCELLED, deliveryOrderRepository.findById(deliveryOrder.getId()).get().getOrderStatus());
        assertEquals(new BigDecimal(101), userRepository.findByLogin(user.getLogin()).get().getAccountBalance());
    }

    @DisplayName("should return delivery order info for user")
    @Test
    public void shouldReturnOrderInfo() throws Exception {
        User user = createUser(new BigDecimal(100));
        Courier courier = createCourier(CourierStatus.FREE);
        DeliveryOrder deliveryOrder = createDeliveryOrder(OrderStatus.CREATED);
        deliveryOrder.setUserLogin(user.getLogin());
        deliveryOrder.setCourier(courier);
        insert(user);
        insert(courier);
        insert(deliveryOrder);
        authenticateAndReturnToken(user.getLogin(), RolesConstants.ROLE_USER, List.of(AuthoritiesConstants.CHANGE_DELIVERY_DESTINATION));

        MvcResult mvcResult = mockmvc.perform(get(USER_DELIVER_ORDER_URL + String.format("/%s/details", deliveryOrder.getId())))
            .andExpect(status().isOk()).andReturn();
        DeliveryOrderDetailsResponseDto responseDto = resultAsObject(mvcResult, DeliveryOrderDetailsResponseDto.class);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        assertEquals(deliveryOrder.getUserLogin(), responseDto.getUserLogin());
        assertEquals(deliveryOrder.getDeliveryCost(), responseDto.getDeliveryCost());
        assertEquals(deliveryOrder.getDestination(), responseDto.getDestination());
        assertEquals(dateTimeFormatter.format(deliveryOrder.getCreatedAt()), dateTimeFormatter.format(responseDto.getCreatedAt()));
        assertEquals(deliveryOrder.getOrderStatus(), responseDto.getOrderStatus());
        assertTrue(StringUtils.isNotBlank(responseDto.getCourierLogin()));
    }

    @DisplayName("should return all delivery orders that belong to user")
    @Test
    public void shouldReturnAllDeliveryOrders() throws Exception {
        User user = createUser(new BigDecimal(100));
        Courier courier = createCourier(CourierStatus.FREE);
        DeliveryOrder deliveryOrder1 = createDeliveryOrder(OrderStatus.CREATED);
        deliveryOrder1.setUserLogin(user.getLogin());
        deliveryOrder1.setCourier(courier);

        DeliveryOrder deliveryOrder2 = createDeliveryOrder(OrderStatus.CREATED);
        deliveryOrder2.setUserLogin(user.getLogin());
        deliveryOrder2.setCourier(courier);

        DeliveryOrder deliveryOrder3 = createDeliveryOrder(OrderStatus.CREATED);
        deliveryOrder3.setUserLogin(randomString(10));
        deliveryOrder3.setCourier(courier);

        insert(user);
        insert(courier);
        insert(deliveryOrder1);
        insert(deliveryOrder2);
        insert(deliveryOrder3);
        authenticateAndReturnToken(user.getLogin(), RolesConstants.ROLE_USER, List.of(AuthoritiesConstants.CURRENT_USER));

        MvcResult mvcResult = mockmvc.perform(get(USER_DELIVER_ORDER_URL))
            .andExpect(status().isOk()).andReturn();
        List<DeliveryOrderResponseDto> responseDtos = resultAsList(mvcResult, DeliveryOrderResponseDto.class);
        assertEquals(responseDtos.size(), 2);
    }

}

