package ru.evendot.toy_shop.model.response;

import lombok.AllArgsConstructor;
import ru.evendot.toy_shop.model.Product;

import java.util.List;

@AllArgsConstructor
public class DataResponseProduct {
    private List<Product> products;
}
