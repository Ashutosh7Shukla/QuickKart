package com.Shopping.dream_shop.controller;


import com.Shopping.dream_shop.exception.ResourceNotFoundException;
import com.Shopping.dream_shop.model.Cart;
import com.Shopping.dream_shop.model.User;
import com.Shopping.dream_shop.response.ApiResponse;
import com.Shopping.dream_shop.service.Cart.ICartService;
import com.Shopping.dream_shop.service.CartItems.ICartItemService;
import com.Shopping.dream_shop.service.user.IUserService;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {

    @Autowired
    private final ICartItemService cartItemService;
    @Autowired
    private final ICartService cartService;
    @Autowired
    private final IUserService userService;

    public CartItemController(ICartItemService cartItemService, ICartService cartService, IUserService userService) {
        this.cartItemService = cartItemService;
        this.cartService = cartService;
        this.userService = userService;
    }


    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItemToCart(
            @RequestParam Long productId,
            @RequestParam int quantity) {
        try {
            User user = userService.getAuthenticateUser();
            Cart cart = cartService.initializeNewCart(user);
            cartItemService.addItemToCart(cart.getId(), productId, quantity);
            return ResponseEntity.ok(new ApiResponse("added success", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), "failed"));
        } catch (JwtException e) {
            return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/cart/{cartId}/item/{productId}/remove")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long productId) {
        try {
            cartItemService.removeItemFromCart(cartId, productId);
            return ResponseEntity.ok(new ApiResponse("remove success", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), "failed"));
        }
    }


    @PutMapping("/cart/{cartId}/item/{productId}/update")
    public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId,
                                                          @PathVariable Long productId,
                                                          @RequestParam int quantity) {
        try {
            cartItemService.updateItemQuantity(cartId, productId, quantity);
            return ResponseEntity.ok(new ApiResponse("update success", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), "failed"));
        }
    }

}
