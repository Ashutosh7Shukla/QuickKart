package com.Shopping.dream_shop.controller;

import com.Shopping.dream_shop.dto.OrderDto;
import com.Shopping.dream_shop.model.Order;
import com.Shopping.dream_shop.response.ApiResponse;
import com.Shopping.dream_shop.service.order.IOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    public final IOrderService orderService;

    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping("/order")
    public ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId) {
        try {
            Order order = orderService.placeOrder(userId);
            OrderDto orderDto= orderService.convertToDto(order);
            return ResponseEntity.ok(new ApiResponse("Item Order Success!", orderDto));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error Occurred!", e.getMessage()));
        }
    }

    @GetMapping("/{orderId}/order")
     public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId){
         try {
             OrderDto order = orderService.getOrder(orderId);
             return ResponseEntity.ok(new ApiResponse("Item Order Success!", order));
         } catch (Exception e) {
             return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Error Occurred!", e.getMessage()));
         }
     }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId){
        try {
            List<OrderDto> orders = orderService.getUserOrders(userId);
            return ResponseEntity.ok(new ApiResponse("Item Order Success!", orders));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Error Occurred!", e.getMessage()));
        }
    }

}
