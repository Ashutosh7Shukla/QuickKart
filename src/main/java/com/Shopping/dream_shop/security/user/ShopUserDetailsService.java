package com.Shopping.dream_shop.security.user;

import com.Shopping.dream_shop.model.User;
import com.Shopping.dream_shop.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShopUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public ShopUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = Optional.ofNullable(userRepository.findByEmail(email)).orElseThrow(()->new UsernameNotFoundException("user not found"));
        return ShopUserDetails.buildUserDetails(user);
    }
}
