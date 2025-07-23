package ru.evendot.warehouse.dto;

import lombok.Data;
import ru.evendot.warehouse.model.Product;

@Data
public class OrderProductDto {
    private Product product;
    private Integer quantity;
}
