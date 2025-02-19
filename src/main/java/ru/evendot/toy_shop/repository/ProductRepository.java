package ru.evendot.toy_shop.repository;

import ru.evendot.toy_shop.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> findAll();

    Optional<Product> save(Product product);

    Optional<Product> findByArticle(Long article);

    Optional<Product> findById(Long id);

    List<Product> findByCategory(String category);

    Boolean existsByArticle(Long article);

    void deleteByArticle(Long article);

//    void deleteById(Long id);

    Optional<Long> updateByArticle(Product product);


    void deleteById(Product product);
}
