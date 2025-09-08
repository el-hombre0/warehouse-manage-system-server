package ru.evendot.warehouse.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double totalAmount;
    private UUID uuid;

    @OneToMany(mappedBy = "order", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<OrderItem> orderItems;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated
    private PaymentStatus paymentStatus;

    @OneToMany(mappedBy = "pk.order")
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    private String comment;
    private Timestamp timeCreation;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToOne
    @JoinColumn(name = "addressId")
    private Address address;
}
