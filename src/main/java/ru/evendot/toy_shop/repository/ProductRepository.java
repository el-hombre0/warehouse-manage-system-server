package ru.evendot.toy_shop.repository;

import ru.evendot.toy_shop.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Optional<List<Product>> findAll();

    Optional<Long> save(Product product);

    Optional<Product> findByArticle(Long article);

    Boolean existsByArticle(Long article);

    void deleteByArticle(Long article);

    Optional<Long> updateByArticle(Product product);
}
