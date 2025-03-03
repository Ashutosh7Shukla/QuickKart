package com.Shopping.dream_shop.service.order;

import com.Shopping.dream_shop.dto.OrderDto;
import com.Shopping.dream_shop.model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);

    OrderDto getOrder(Long orderId);

    List<OrderDto> getUserOrders(Long userId);

    OrderDto convertToDto(Order order);
}
