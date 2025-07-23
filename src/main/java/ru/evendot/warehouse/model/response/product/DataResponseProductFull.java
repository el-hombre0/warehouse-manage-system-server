package ru.evendot.warehouse.model.response.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.evendot.warehouse.model.Product;

@Data
@AllArgsConstructor
public class DataResponseProductFull {
    private Product product;
}
