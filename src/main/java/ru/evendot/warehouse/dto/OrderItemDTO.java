package ru.evendot.warehouse.dto;

import lombok.Data;

@Data
public class OrderItemDTO {
    private Long productId;
    private ProductDTO product;
    private int quantity;
    private Double price;
}
