package com.Shopping.dream_shop.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public class OrderDto {
    private Long orderId;
    private LocalDate orderDate;
    private BigDecimal totalAmount;
    private String orderStatus; // String to avoid exposing enum directly
    private Set<OrderItemDto> orderItems; // Nested DTO for order items
    private Long userId;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Set<OrderItemDto> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItemDto> orderItems) {
        this.orderItems = orderItems;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
