package ru.evendot.warehouse.model.request.user;

import lombok.Data;
import ru.evendot.warehouse.model.Address;

import java.util.HashMap;
import java.util.Map;

@Data
public class UserUpdateRequest {
    private String firstname;
    private String lastname;
    private String middlename;
    private Map<String, String> socialMedia = new HashMap<>();
    private Address userAddress;
}
