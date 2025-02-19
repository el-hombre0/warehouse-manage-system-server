package ru.evendot.toy_shop.repository;

import ru.evendot.toy_shop.model.Category;

import java.util.Optional;

public interface CategoryRepository {
    Optional<Category> findByName(String name);
    Optional<Category> save(Category category);
}
