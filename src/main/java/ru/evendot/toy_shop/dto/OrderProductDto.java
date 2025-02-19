package ru.evendot.toy_shop.dto;

import lombok.Data;
import ru.evendot.toy_shop.model.Product;

@Data
public class OrderProductDto {
    private Product product;
    private Integer quantity;
}
