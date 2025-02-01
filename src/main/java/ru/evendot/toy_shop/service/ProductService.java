package ru.evendot.toy_shop.service;

import ru.evendot.toy_shop.exception.CustomException;
import ru.evendot.toy_shop.model.Product;
import ru.evendot.toy_shop.model.request.CreateProduct;

import java.util.List;

public interface ProductService {
    List<Product> getProducts();
    Long save(CreateProduct product) throws CustomException;
}
