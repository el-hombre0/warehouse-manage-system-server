package ru.evendot.warehouse.repository.impl;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.evendot.warehouse.model.Category;
import ru.evendot.warehouse.repository.CategoryRepository;

import java.util.Optional;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {
    private final JdbcTemplate jdbcTemplate;
    public CategoryRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final String SELECT_FROM_CATEGORY_WHERE_NAME = "SELECT * FROM categories WHERE name = ?";
    private final String INSERT_INTO_CATEGORY = "INSERT INTO categories (name) VALUES (?)";

    @Override
    public Optional<Category> findByName(String name) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_FROM_CATEGORY_WHERE_NAME, new BeanPropertyRowMapper<>(Category.class), name));
    }

    @Override
    public Optional<Category> save(Category category){
        jdbcTemplate.update(INSERT_INTO_CATEGORY, category.getName());
        return Optional.of(category);
    }
}
