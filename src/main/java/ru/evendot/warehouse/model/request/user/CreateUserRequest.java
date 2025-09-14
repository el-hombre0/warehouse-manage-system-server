package ru.evendot.warehouse.model.request.user;

import lombok.Data;
import org.hibernate.annotations.NaturalId;
import ru.evendot.warehouse.model.Address;

import java.util.HashMap;
import java.util.Map;

@Data
public class CreateUserRequest {
    private String firstname;
    private String lastname;
    private String middlename;
    private String phoneNumber;
    private Map<String, String> socialMedia = new HashMap<>();

    @NaturalId
    private String email;
    private String password;
    private Address userAddress;
}
