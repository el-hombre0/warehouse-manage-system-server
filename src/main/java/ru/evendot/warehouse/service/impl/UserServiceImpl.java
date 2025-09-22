package ru.evendot.warehouse.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.evendot.warehouse.exception.ResourceAlreadyExistsException;
import ru.evendot.warehouse.exception.ResourceNotFoundException;
import ru.evendot.warehouse.model.User;
import ru.evendot.warehouse.model.request.user.CreateUserRequest;
import ru.evendot.warehouse.model.request.user.UserUpdateRequest;
import ru.evendot.warehouse.repository.UserRepository;
import ru.evendot.warehouse.service.UserService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User with id " + userId + " not found!"));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user -> !userRepository.existsByEmail(request.getEmail()))
                .map(req -> {
                    User user = new User();
                    user.setFirstname(req.getFirstname());
                    user.setLastname(req.getLastname());
                    user.setMiddlename(req.getMiddlename());
                    user.setPhoneNumber(req.getPhoneNumber());
                    user.setSocialMedia(req.getSocialMedia());
                    user.setUserAddresses(req.getUserAddresses());
                    user.setEmail(req.getEmail());
                    user.setPassword(req.getPassword());
                    return userRepository.save(user);
                }).orElseThrow(() -> new ResourceAlreadyExistsException("User already exists!"));
    }

    @Override
    public User updateUser(UserUpdateRequest request, Long userId) {
        return userRepository.findById(userId).map(existinguser -> {
            existinguser.setFirstname(request.getFirstname());
            existinguser.setLastname(request.getLastname());
            existinguser.setMiddlename(request.getMiddlename());
            existinguser.setUserAddresses(request.getUserAddresses());
            existinguser.setSocialMedia(request.getSocialMedia());
            return userRepository.save(existinguser);
        }).orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found!"));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository::delete, () -> {
            throw new ResourceNotFoundException("User with id " + userId + " not found!");
        });
    }
}
