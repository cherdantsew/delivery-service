package com.orderengine.deliver.deliverservice.service;

import com.orderengine.deliver.deliverservice.exception.http.BadRequestException;
import com.orderengine.deliver.deliverservice.exception.http.UnauthorizedException;
import com.orderengine.deliver.deliverservice.mapper.DeliveryOrderMapper;
import com.orderengine.deliver.deliverservice.model.dto.ChangeDeliveryDestinationRequestDto;
import com.orderengine.deliver.deliverservice.model.dto.ChangeOrderStatusRequestDto;
import com.orderengine.deliver.deliverservice.model.dto.DeliveryOrderDetailsResponseDto;
import com.orderengine.deliver.deliverservice.model.dto.DeliveryOrderRequestDto;
import com.orderengine.deliver.deliverservice.model.dto.DeliveryOrderResponseDto;
import com.orderengine.deliver.deliverservice.model.entity.DeliveryOrder;
import com.orderengine.deliver.deliverservice.model.entity.User;
import com.orderengine.deliver.deliverservice.model.enumeration.OrderStatus;
import com.orderengine.deliver.deliverservice.model.enumeration.RolesConstants;
import com.orderengine.deliver.deliverservice.repository.DeliveryOrderRepository;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeliveryOrderService {

    private final DeliveryOrderRepository repository;
    private final UserService userService;
    private final DeliveryOrderMapper mapper;

    public DeliveryOrderService(
            DeliveryOrderRepository repository,
            UserService userService,
            DeliveryOrderMapper mapper
    ) {
        this.repository = repository;
        this.userService = userService;
        this.mapper = mapper;
    }

    public void createDeliveryOrder(DeliveryOrderRequestDto requestDto) {
        repository.save(DeliveryOrder.fromRequestDto(requestDto));
    }

    public List<DeliveryOrder> findAllOrdersWaitingForCourier() {
        return repository.findAllByCourierIdIsNull();
    }

    public void save(DeliveryOrder deliveryOrder) {
        repository.save(deliveryOrder);
    }

    public void changeOrderDestination(ChangeDeliveryDestinationRequestDto requestDto) {
        DeliveryOrder deliveryOrder = repository.findById(requestDto.getOrderId()).orElseThrow();
        //if courier already delivers parcel
        if (deliveryOrder.getCourier() != null && OrderStatus.DELIVER_IN_PROGRESS == deliveryOrder.getOrderStatus()) {
            throw new BadRequestException("Delivery is in progress. Too late to change delivery address.");
        }
        deliveryOrder.setDestination(requestDto.getNewDestination());
        repository.save(deliveryOrder);
    }

    public DeliveryOrderDetailsResponseDto getDeliveryOrderById(Long orderId, String currentUserLogin) {
        return repository.findDeliveryOrderById(orderId, currentUserLogin).orElseThrow();
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

    public List<DeliveryOrderResponseDto> findAllByUserLogin(String currentUserLogin) {
        return mapper.toDto(repository.findAllByUserLogin(currentUserLogin));
    }

    public List<DeliveryOrderResponseDto> findAll() {
        return mapper.toDto(repository.findAll());
    }

    public DeliveryOrderResponseDto changeOrderStatus(ChangeOrderStatusRequestDto requestDto) {
        DeliveryOrder deliveryOrder = repository.findById(requestDto.getOrderId()).orElseThrow();
        deliveryOrder.setOrderStatus(requestDto.getOrderStatus());
        return mapper.toDto(repository.saveAndFlush(deliveryOrder));
    }

    public DeliveryOrder findById(Long orderId) {
        return repository.findById(orderId).orElseThrow();
    }
}
