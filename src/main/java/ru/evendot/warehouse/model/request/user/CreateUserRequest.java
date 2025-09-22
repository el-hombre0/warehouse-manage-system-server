package ru.evendot.warehouse.model.request.user;

import lombok.Data;
import org.hibernate.annotations.NaturalId;
import ru.evendot.warehouse.model.Address;
import ru.evendot.warehouse.model.UserSocialMedia;

import java.util.Set;

@Data
public class CreateUserRequest {

    @NaturalId
    private String email;

    private String firstname;
    private String lastname;
    private String middlename;
    private String phoneNumber;
    private UserSocialMedia socialMedia;
    private String password;
    private Set<Address> userAddresses;
}
