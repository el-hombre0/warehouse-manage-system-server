package ru.evendot.warehouse.dto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.Set;

@Data
public class CartDTO {
    private Long id;
    private Double totalAmount;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItemDTO> cartItems;
}
