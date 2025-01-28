package ru.evendot.toy_shop.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
//import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Double cost;
    private PayMethods payMethod;

//    @OneToMany
//    private List<Product> products;

//    @OneToOne
//    private User user;

    private String comment;
    private LocalDateTime dateTime;

    private Statuses status;

//    @OneToOne
//    private Address address;
}
