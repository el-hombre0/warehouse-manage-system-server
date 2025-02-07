package ru.evendot.toy_shop.model.response.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.evendot.toy_shop.model.Product;

import java.util.List;

@Data
@AllArgsConstructor
public class DataResponseProductList {
    private List<Product> products;
}
