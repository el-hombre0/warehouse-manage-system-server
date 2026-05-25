package ru.evendot.userservice.Broker.Events;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserRegisteredEvent extends BaseEvent{
    private Long userId;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private String registrationDate;
    private String registrationSource;
    private Boolean isPhoneVerified;
    private String accountStatus;

    public UserRegisteredEvent(){
        setEventType("USER_REGISTERED");
    }
}
