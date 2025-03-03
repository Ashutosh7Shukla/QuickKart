package com.Shopping.dream_shop.service.user;

import com.Shopping.dream_shop.dto.UserDto;
import com.Shopping.dream_shop.exception.ResourceNotFoundException;
import com.Shopping.dream_shop.model.Role;
import com.Shopping.dream_shop.model.User;
import com.Shopping.dream_shop.repository.RoleRepository;
import com.Shopping.dream_shop.repository.UserRepository;
import com.Shopping.dream_shop.request.CreateUserRequest;
import com.Shopping.dream_shop.request.UpdateUserRequest;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    public final UserRepository userRepository;
    public final ModelMapper modelMapper;
    public final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request).filter(user -> !userRepository.existsByEmail(request.getEmail())).map(req -> {
            User user = new User();
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
             Collection<Role> roles = roleRepository.findByName(request.getRole());
             user.setRoles(roles);
            return userRepository.save(user);
        }).orElseThrow(() -> new ResourceNotFoundException("user already exists by email :-" + request.getEmail()));
    }

    @Override
    public User updateUser(UpdateUserRequest request, Long userId) {
        return userRepository.findById(userId).map(existingUser -> {
            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository::delete, () -> {
            throw new ResourceNotFoundException("user not found");
        });
    }

    @Override
    public UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public User getAuthenticateUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }
}
