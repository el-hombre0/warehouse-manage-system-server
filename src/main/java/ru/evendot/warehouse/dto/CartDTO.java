package ru.evendot.warehouse.dto;


import lombok.Data;

import java.util.Set;

@Data
public class CartDTO {
    private Long id;
    private Double totalAmount;
    private Set<CartItemDTO> cartItems;
}
