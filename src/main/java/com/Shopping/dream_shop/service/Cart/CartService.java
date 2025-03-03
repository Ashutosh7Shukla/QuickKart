package com.Shopping.dream_shop.service.Cart;

import com.Shopping.dream_shop.dto.CartDto;
import com.Shopping.dream_shop.exception.ResourceNotFoundException;
import com.Shopping.dream_shop.model.Cart;
import com.Shopping.dream_shop.model.User;
import com.Shopping.dream_shop.repository.CartItemRepository;
import com.Shopping.dream_shop.repository.CartRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service

public class CartService implements ICartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final AtomicLong cartIdGenerator = new AtomicLong(0);
    private final ModelMapper modelMapper;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, ModelMapper modelMapper) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }


    @Transactional
    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cart.getItems().clear();
        cart.setTotalAmount(BigDecimal.ZERO);
        cartRepository.save(cart);
        cartRepository.deleteById(id);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }

    @Override
    public Cart initializeNewCart(User user) {
        return Optional.ofNullable(getCartByUserId(user.getId()))
                .orElseGet(() -> {
            Cart cart = new Cart();
            cart.setUser(user);
            return cartRepository.save(cart);
        });

    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public CartDto convertToDto(Cart cart)
    {
        return modelMapper.map(cart , CartDto.class);
    }
}
