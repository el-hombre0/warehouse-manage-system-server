package ru.evendot.userservice.DTOs;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private String middlename;
    private String phoneNumber;

    private UserSocialMediaDTO socialMedia;

    private String email;

    private String jwt;

//    private CartDTO cart;

//    private List<OrderDTO> orders;

//    private AddressDTO address;
}
