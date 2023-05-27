package com.orderengine.deliver.deliverservice.service;

import com.orderengine.deliver.deliverservice.exception.http.BadRequestException;
import com.orderengine.deliver.deliverservice.exception.http.UnauthorizedException;
import com.orderengine.deliver.deliverservice.mapper.DeliveryOrderMapper;
import com.orderengine.deliver.deliverservice.model.dto.ChangeDeliveryDestinationRequestDto;
import com.orderengine.deliver.deliverservice.model.dto.DeliveryOrderDetailsResponseDto;
import com.orderengine.deliver.deliverservice.model.dto.DeliveryOrderResponseDto;
import com.orderengine.deliver.deliverservice.model.entity.DeliveryOrder;
import com.orderengine.deliver.deliverservice.model.entity.User;
import com.orderengine.deliver.deliverservice.model.enumeration.AuthoritiesConstants;
import com.orderengine.deliver.deliverservice.model.enumeration.OrderStatus;
import com.orderengine.deliver.deliverservice.model.enumeration.RolesConstants;
import com.orderengine.deliver.deliverservice.repository.DeliveryOrderRepository;
import com.orderengine.deliver.deliverservice.utils.SecurityUtils;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDeliveryOrderService extends AbstractDeliveryOrderService {

    public UserDeliveryOrderService(DeliveryOrderRepository repository, UserService userService, DeliveryOrderMapper mapper) {
        super(repository, userService, mapper);
    }

    public void changeOrderDestination(Long id, ChangeDeliveryDestinationRequestDto requestDto) {
        DeliveryOrder deliveryOrder = repository.findById(id).orElseThrow();

        if (!SecurityUtils.isCurrentUserInPermission(AuthoritiesConstants.CHANGE_DELIVERY_DESTINATION)) {
            throw new BadRequestException("You have no permission to change delivery destination");
        }
        if (deliveryOrder.getCourier() != null && OrderStatus.DELIVER_IN_PROGRESS == deliveryOrder.getOrderStatus()) {
            throw new BadRequestException("Delivery is in progress. Too late to change delivery address.");
        }
        deliveryOrder.setDestination(requestDto.getNewDestination());

        repository.save(deliveryOrder);
    }

    @Transactional
    public void cancelDeliveryOrder(Long orderId, String currentUserLogin, RolesConstants currentUserRole) {
        DeliveryOrder deliveryOrder = repository.findById(orderId).orElseThrow();
        //if courier already delivers order
        if (deliveryOrder.getCourier() != null && OrderStatus.DELIVER_IN_PROGRESS == deliveryOrder.getOrderStatus()) {
            throw new BadRequestException("Delivery is in progress. Too late to cancel delivery order.");
        }

        if (!Objects.equals(currentUserLogin, deliveryOrder.getUserLogin()) || RolesConstants.ROLE_ADMIN != currentUserRole) {
            throw new UnauthorizedException("You have no permission to cancel this order.");
        }

        deliveryOrder.setOrderStatus(OrderStatus.CANCELLED);
        User user = userService.getByLogin(deliveryOrder.getUserLogin());
        user.setAccountBalance(user.getAccountBalance().add(deliveryOrder.getDeliveryCost()));
        repository.save(deliveryOrder);
        userService.save(user);
    }

    public DeliveryOrderDetailsResponseDto getOrderDetails(Long orderId, String currentUserLogin) {
        return repository.findDeliveryOrderByIdAndUserLogin(orderId, currentUserLogin).orElseThrow();
    }

    public List<DeliveryOrderResponseDto> findAllByUserLogin(String currentUserLogin) {
        return mapper.toDto(repository.findAllByUserLogin(currentUserLogin));
    }

}
