package ru.evendot.toy_shop.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.evendot.toy_shop.model.Product;

import java.util.List;

@Data
@AllArgsConstructor
public class DataResponseProduct {
    private List<Product> products;
}
