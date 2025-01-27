package ru.evendot.toy_shop.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstname;
    private String lastname;
    private String middlename;
    private String phoneNumber;
    private String email;

//    @OneToOne
//    @JoinColumn(name = "")
//    private Address userAddress;
}
