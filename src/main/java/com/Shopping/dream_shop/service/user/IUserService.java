package com.Shopping.dream_shop.service.user;

import com.Shopping.dream_shop.dto.UserDto;
import com.Shopping.dream_shop.model.User;
import com.Shopping.dream_shop.request.CreateUserRequest;
import com.Shopping.dream_shop.request.UpdateUserRequest;

public interface IUserService {
    User getUserById(Long userId);

    User createUser(CreateUserRequest request);

    User updateUser(UpdateUserRequest request, Long userId);

    void deleteUser(Long userId);

    UserDto convertToDto(User user);

    User getAuthenticateUser();
}
