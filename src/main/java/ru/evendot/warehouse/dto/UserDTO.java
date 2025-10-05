package ru.evendot.warehouse.dto;

import lombok.Data;
import java.util.List;

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

    private AddressDTO address;
}
