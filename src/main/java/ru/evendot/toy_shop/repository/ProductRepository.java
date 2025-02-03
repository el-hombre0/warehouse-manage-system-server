package ru.evendot.toy_shop.repository;

import ru.evendot.toy_shop.model.Product;
import ru.evendot.toy_shop.model.request.CreateProduct;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Optional<List<Product>> findAll();

    Optional<Long> save(Product product);

    Optional<Product> findByArticle(CreateProduct product);

    Boolean existsByArticle(Long article);

    int deleteByArticle(Long article);
}
