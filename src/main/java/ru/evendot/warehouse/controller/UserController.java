package ru.evendot.warehouse.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.evendot.warehouse.exception.ResourceAlreadyExistsException;
import ru.evendot.warehouse.exception.ResourceNotFoundException;
import ru.evendot.warehouse.model.request.user.CreateUserRequest;
import ru.evendot.warehouse.model.request.user.UserUpdateRequest;
import ru.evendot.warehouse.model.response.DataResponse;
import ru.evendot.warehouse.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/user/{id}")
    public ResponseEntity<DataResponse> getUser(@PathVariable Long userId){
        try {
                return ResponseEntity.ok(
                        new DataResponse("User received successfully!", userService.getUserById(userId))
                );
    }
        catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new DataResponse("User with id " + userId + " not found!", e));
        }
    }

    @PostMapping("/user")
    public ResponseEntity<DataResponse> createUser(@RequestBody CreateUserRequest request){
        try {
            return ResponseEntity.ok(new DataResponse("User created successfully!", userService.createUser(request)));
        } catch (ResourceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new DataResponse("Error occurred!", null ));
        }
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<DataResponse> updateUser(@RequestParam UserUpdateRequest request, @PathVariable Long userId){
        try {
            return ResponseEntity.ok(new DataResponse(
                    "User updated successfully!", userService.updateUser(request, userId)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new DataResponse("User with id " + userId + " not found!", null ));
        }
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<DataResponse> deleteUser(@PathVariable Long userId){
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(new DataResponse("User deleted successfully!", null));
        } catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new DataResponse("User with id " + userId + " not found!", e));
        }
    }


}
