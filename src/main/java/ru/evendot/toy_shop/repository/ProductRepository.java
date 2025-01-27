package ru.evendot.toy_shop.repository;

import ru.evendot.toy_shop.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Optional<List<Product>> findAll();
}
