package ru.evendot.warehouse.model.response.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.evendot.warehouse.model.Product;

import java.util.List;

@Data
@AllArgsConstructor
public class DataResponseProductList {
    private List<Product> products;
}
