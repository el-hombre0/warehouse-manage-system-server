package ru.evendot.warehouse.repository;

import ru.evendot.warehouse.model.Category;

import java.util.Optional;

public interface CategoryRepository {
    Optional<Category> findByName(String name);
    Optional<Category> save(Category category);
}
