package ru.evendot.toy_shop.model.response.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.evendot.toy_shop.model.Product;

@Data
@AllArgsConstructor
public class DataResponseProductFull {
    private Product product;
}
