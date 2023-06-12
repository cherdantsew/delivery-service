package com.deliverengine.deliver.service;

import com.deliverengine.core.enumeration.AuthoritiesConstants;
import com.deliverengine.core.enumeration.RolesConstants;
import com.deliverengine.core.exception.http.BadRequestException;
import com.deliverengine.core.exception.http.ForbiddenException;
import com.deliverengine.core.exception.http.UnauthorizedException;
import com.deliverengine.core.utils.SecurityUtils;
import com.deliverengine.deliver.mapper.DeliveryOrderMapper;
import com.deliverengine.deliver.model.dto.ChangeDeliveryDestinationRequestDto;
import com.deliverengine.deliver.model.dto.DeliveryOrderDetailsResponseDto;
import com.deliverengine.deliver.model.dto.DeliveryOrderRequestDto;
import com.deliverengine.deliver.model.dto.DeliveryOrderResponseDto;
import com.deliverengine.deliver.model.entity.DeliveryOrder;
import com.deliverengine.deliver.model.entity.User;
import com.deliverengine.deliver.model.enumeration.OrderStatus;
import com.deliverengine.deliver.repository.DeliveryOrderRepository;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDeliveryOrderService extends AbstractDeliveryOrderService {

    public UserDeliveryOrderService(DeliveryOrderRepository repository, UserService userService, DeliveryOrderMapper mapper) {
        super(repository, userService, mapper);
    }

    @Transactional
    public void createDeliveryOrder(DeliveryOrderRequestDto requestDto) {
        validateOrderCreation(requestDto);
        User user = userService.getByLogin(requestDto.getUserLogin());
        if (user.getAccountBalance().compareTo(requestDto.getDeliveryCost()) < 0) {
            throw new BadRequestException("Pop up your account balance to order that delivery.");
        }
        repository.save(DeliveryOrder.fromRequestDto(requestDto));
        user.setAccountBalance(user.getAccountBalance().subtract(requestDto.getDeliveryCost()));
        userService.save(user);
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


    protected void validateOrderCreation(DeliveryOrderRequestDto requestDto) {
        if (!SecurityUtils.isCurrentUserInPermission(AuthoritiesConstants.CREATE_DELIVERY_ORDER)) {
            throw new ForbiddenException("No permission to create delivery orders.");
        }
        if (!Objects.equals(requestDto.getUserLogin(), SecurityUtils.currentUserLoginOrException())) {
            throw new ForbiddenException("You can only create delivery orders to yourself.");
        }
    }
}
