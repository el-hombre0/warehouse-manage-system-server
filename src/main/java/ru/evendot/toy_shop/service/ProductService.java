package ru.evendot.toy_shop.service;

import ru.evendot.toy_shop.exception.CustomException;
import ru.evendot.toy_shop.model.Product;
import ru.evendot.toy_shop.model.request.product.CreateProduct;
import ru.evendot.toy_shop.model.response.product.DataResponseProduct;

import java.util.List;

public interface ProductService {
    List<Product> getProducts();

    Product getProduct(Long article);

    Long save(CreateProduct product) throws CustomException;

    void deleteByArticle(Long article);

    DataResponseProduct updateProduct(CreateProduct product);
}
