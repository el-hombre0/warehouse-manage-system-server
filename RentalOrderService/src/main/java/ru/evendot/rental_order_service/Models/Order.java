package ru.evendot.rental_order_service.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
    private Set<OrderItem> orderItems = new HashSet<>();

//    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
//    private Long paymentMethod;

//    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
//    private Long paymentStatus;

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
    private Long userId;

    private Long issuedByUserId;

    private String comment;
    private Timestamp timeCreation;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

//    @OneToOne
//    @JoinColumn(name = "address_id")
//    private Address address;
    private Long rentalPointId;

    private LocalDateTime startDateTime;
    private LocalDateTime plannedEndDateTime;
    private LocalDateTime actualEndDateTime;

}
