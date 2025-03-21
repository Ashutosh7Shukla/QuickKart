package com.Shopping.dream_shop.service.Cart;

import com.Shopping.dream_shop.dto.CartDto;
import com.Shopping.dream_shop.model.Cart;
import com.Shopping.dream_shop.model.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);

    void clearCart(Long id);

    BigDecimal getTotalPrice(Long id);

    Cart initializeNewCart(User user);

    Cart getCartByUserId(Long userId);

    CartDto convertToDto(Cart cart);
}
