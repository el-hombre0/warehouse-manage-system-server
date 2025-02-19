package ru.evendot.toy_shop.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double cost;
    private UUID uuid;
    private PayMethods payMethod;

    @OneToMany(mappedBy = "pk.order")
    private List<OrderProduct> orderProducts = new ArrayList<>();


    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    private String comment;
    private Timestamp timeCreation;


    private Statuses status;

    @OneToOne
    @JoinColumn(name = "addressId")
    private Address address;
}
