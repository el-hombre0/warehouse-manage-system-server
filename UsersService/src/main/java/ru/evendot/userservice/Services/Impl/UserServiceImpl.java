package ru.evendot.userservice.Services.Impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.evendot.userservice.Broker.Events.UserRegisteredEvent;
import ru.evendot.userservice.Broker.Producers.UserEventsProducer;
import ru.evendot.userservice.Configuration.Security.JwtService;
import ru.evendot.userservice.DTOs.UserDTO;
import ru.evendot.userservice.Models.AccountStatuses;
import ru.evendot.userservice.Models.Requests.AuthenticateUserRequest;
import ru.evendot.userservice.Models.Responses.AuthenticationResponse;
import ru.evendot.userservice.Models.User;
import ru.evendot.userservice.Exceptions.ResourceAlreadyExistsException;
import ru.evendot.userservice.Exceptions.ResourceNotFoundException;
import ru.evendot.userservice.Models.Requests.CreateUserRequest;
import ru.evendot.userservice.Models.Requests.UserUpdateRequest;
import ru.evendot.userservice.Repositories.UserRepository;
import ru.evendot.userservice.Services.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    private UserEventsProducer producer;

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
                    user.setAddress(req.getAddress());
                    user.setEmail(req.getEmail());
                    user.setPassword(req.getPassword());
                    user.setRegistrationDate(LocalDateTime.now());
                    user.setRegistrationIp("127.0.0.1");
                    user.setRegistrationSource("web-app");
                    user.setAccountStatus(AccountStatuses.ACTIVE);

                    UserRegisteredEvent event = new UserRegisteredEvent();
                    event.setUserId(user.getId());
                    event.setEmail(user.getEmail());
                    event.setPhone(user.getPhoneNumber());
                    event.setFirstName(user.getFirstname());
                    event.setLastName(user.getLastname());
                    event.setRegistrationDate(user.getRegistrationDate().toString());
                    event.setRegistrationSource(user.getRegistrationSource());
                    event.setIsPhoneVerified(Boolean.FALSE);
                    event.setAccountStatus(user.getAccountStatus().toString());
                    producer.sendUserRegistered(event);
                    return userRepository.save(user);
                }).orElseThrow(() -> new ResourceAlreadyExistsException("User already exists!"));
    }

    @Override
    public AuthenticationResponse authenticateUser(AuthenticateUserRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .firstName(user.getFirstname())
                .lastName(user.getLastname())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .build();
    }

    @Override
    public AuthenticationResponse checkJWT(String jwtString) {
        String jwt = jwtString.substring(7);
        var userEmail = jwtService.extractUsername(jwt);
        var user = userRepository.findByEmail(userEmail).orElseThrow();
        return AuthenticationResponse.builder()
                .token(jwt)
                .firstName(user.getFirstname())
                .lastName(user.getLastname())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .build();
    }

    @Override
    public User updateUser(UserUpdateRequest request, Long userId) {
        return userRepository.findById(userId).map(existinguser -> {
            existinguser.setFirstname(request.getFirstname());
            existinguser.setLastname(request.getLastname());
            existinguser.setMiddlename(request.getMiddlename());
            existinguser.setAddress(request.getAddress());
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

    @Override
    public UserDTO convertUserToDTO(User user) {
        var jwtToken = jwtService.generateToken(user);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setJwt(jwtToken);
        return userDTO;
    }
}
