package ru.evendot.warehouse.dto;

import lombok.Data;
import ru.evendot.warehouse.model.Address;
import ru.evendot.warehouse.model.OrderStatus;
import ru.evendot.warehouse.model.PaymentMethod;
import ru.evendot.warehouse.model.PaymentStatus;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@Data
public class OrderDTO {
    private Long orderId;
    private Double totalAmount;
    private UUID uuid;
    private Long userId;
    private Set<OrderItemDTO> orderItems;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private String comment;
    private Timestamp timeCreation;
    private OrderStatus orderStatus;
    private Address address;
}
