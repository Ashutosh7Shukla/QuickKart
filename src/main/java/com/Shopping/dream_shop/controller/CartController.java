package com.Shopping.dream_shop.controller;

import com.Shopping.dream_shop.dto.CartDto;
import com.Shopping.dream_shop.exception.ResourceNotFoundException;
import com.Shopping.dream_shop.model.Cart;
import com.Shopping.dream_shop.response.ApiResponse;
import com.Shopping.dream_shop.service.Cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("${api.prefix}/carts")
public class CartController {
    @Autowired
    private final ICartService cartService;

    public CartController(ICartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{cartId}/my-cart")
    public ResponseEntity<ApiResponse> getCart(@PathVariable Long cartId) {
        try {
            Cart cart = cartService.getCart(cartId);
            CartDto cartDto = cartService.convertToDto(cart);
            return ResponseEntity.ok(new ApiResponse("success", cartDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), "failed"));
        }
    }

    @DeleteMapping("/{cartId}/clear")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long cartId) {
        try {
            cartService.clearCart(cartId);
            return ResponseEntity.ok(new ApiResponse("clear cart success", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), "failed"));
        }
    }

    @GetMapping("/{cartId}/cart/total-price")
    public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable Long cartId) {
        try {
        BigDecimal amount =cartService.getTotalPrice(cartId);
        return ResponseEntity.ok(new ApiResponse("total price :", amount));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), "failed"));
        }
    }
}
