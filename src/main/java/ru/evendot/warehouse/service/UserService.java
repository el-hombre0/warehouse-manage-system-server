package ru.evendot.warehouse.service;

import ru.evendot.warehouse.model.User;
import ru.evendot.warehouse.model.request.user.CreateUserRequest;
import ru.evendot.warehouse.model.request.user.UserUpdateRequest;

public interface UserService {
    User getUserById(Long userId);

    User createUser(CreateUserRequest request);

    User updateUser(UserUpdateRequest request, Long userId);

    void deleteUser(Long userId);

}
