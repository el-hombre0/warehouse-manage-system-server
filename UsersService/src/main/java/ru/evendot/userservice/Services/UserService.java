package ru.evendot.userservice.Services;

import ru.evendot.userservice.Models.Requests.AuthenticateUserRequest;
import ru.evendot.userservice.Models.Responses.AuthenticationResponse;
import ru.evendot.userservice.Models.User;
import ru.evendot.userservice.DTOs.UserDTO;
import ru.evendot.userservice.Models.Requests.CreateUserRequest;
import ru.evendot.userservice.Models.Requests.UserUpdateRequest;

public interface UserService {
    User getUserById(Long userId);

    User createUser(CreateUserRequest request);

    AuthenticationResponse authenticateUser(AuthenticateUserRequest request);

    AuthenticationResponse checkJWT(String jwtString);

    User updateUser(UserUpdateRequest request, Long userId);

    void deleteUser(Long userId);

    UserDTO convertUserToDTO(User user);

}
