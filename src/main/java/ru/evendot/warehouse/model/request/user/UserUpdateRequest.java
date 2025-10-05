package ru.evendot.warehouse.model.request.user;

import lombok.Data;
import ru.evendot.warehouse.model.Address;
import ru.evendot.warehouse.model.UserSocialMedia;


@Data
public class UserUpdateRequest {
    private String firstname;
    private String lastname;
    private String middlename;
    private UserSocialMedia socialMedia;
    private Address address;
}
