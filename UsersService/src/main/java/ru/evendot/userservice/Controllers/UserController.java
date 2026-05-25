package ru.evendot.userservice.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.evendot.userservice.Exceptions.ResourceAlreadyExistsException;
import ru.evendot.userservice.Exceptions.ResourceNotFoundException;
import ru.evendot.userservice.Models.Requests.AuthenticateUserRequest;
import ru.evendot.userservice.Models.Requests.CreateUserRequest;
import ru.evendot.userservice.Models.Requests.UserUpdateRequest;
import ru.evendot.userservice.Models.Responses.AuthenticationResponse;
import ru.evendot.userservice.Models.Responses.DataResponse;
import ru.evendot.userservice.Models.User;
import ru.evendot.userservice.Services.UserService;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<DataResponse> getUser(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            return ResponseEntity.ok(
                    new DataResponse("User received successfully!", userService.convertUserToDTO(user))
            );
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new DataResponse("User with id " + userId + " not found!", e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<DataResponse> createUser(@RequestBody CreateUserRequest request) {
        try {
            User user = userService.createUser(request);
            return ResponseEntity.ok(new DataResponse("User created successfully!",
                    userService.convertUserToDTO(user)));
        } catch (ResourceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new DataResponse("Error occurred!",
                    null));
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<DataResponse> authenticateUser(@RequestBody AuthenticateUserRequest request){
        try {
            return ResponseEntity.ok(new DataResponse("User authenticated successfully!",
                    new AuthenticationResponse(userService.authenticateUser(request))));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<DataResponse> updateUser(@RequestParam UserUpdateRequest request, @PathVariable Long userId) {
        try {
            User user = userService.updateUser(request, userId);
            return ResponseEntity.ok(new DataResponse(
                    "User updated successfully!", userService.convertUserToDTO(user)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new DataResponse("User with id " + userId + " not found!", null));
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<DataResponse> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(new DataResponse("User deleted successfully!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new DataResponse("User with id " + userId + " not found!", e));
        }
    }


}
