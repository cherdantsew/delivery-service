package com.deliverengine.deliver.controller;

import com.deliverengine.core.enumeration.AuthoritiesConstants;
import com.deliverengine.core.enumeration.RolesConstants;
import com.deliverengine.deliver.SpringBootApplicationTest;
import com.deliverengine.deliver.model.dto.CourierResponseDto;
import com.deliverengine.deliver.model.entity.Courier;
import com.deliverengine.deliver.model.enumeration.CourierStatus;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import static com.deliverengine.deliver.helper.CourierHelper.createCourier;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CourierControllerTests extends SpringBootApplicationTest {
    public static final String ADMIN_COURIERS_URL = "/admin/deliver-service/couriers";

    @DisplayName("should return all couriers to admin")
    @Test
    public void shouldReturnAllCouriers() throws Exception {
        Courier courier1 = createCourier(CourierStatus.FREE);
        Courier courier2 = createCourier(CourierStatus.BUSY);
        Courier courier3 = createCourier(CourierStatus.BUSY);
        insert(courier1);
        insert(courier2);
        insert(courier3);
        authenticateAndReturnToken(RolesConstants.ROLE_ADMIN, List.of(AuthoritiesConstants.VIEW_ALL_COURIERS));
        MvcResult mvcResult = mockmvc.perform(get(ADMIN_COURIERS_URL))
            .andExpect(status().isOk()).andReturn();
        List<CourierResponseDto> responseDtos = resultAsList(mvcResult, CourierResponseDto.class);
        assertEquals(3, responseDtos.size());
    }
}
