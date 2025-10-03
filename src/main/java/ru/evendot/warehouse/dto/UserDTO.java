package ru.evendot.warehouse.dto;

import lombok.Data;
import java.util.List;
import java.util.Set;

@Data
public class UserDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private String middlename;
    private String phoneNumber;

    private UserSocialMediaDTO socialMedia;

    private String email;

    private CartDTO cart;

    private List<OrderDTO> orders;

    private Set<AddressDTO> userAddresses;
}
