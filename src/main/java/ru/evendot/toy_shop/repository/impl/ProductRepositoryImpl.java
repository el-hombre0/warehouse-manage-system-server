package ru.evendot.toy_shop.repository.impl;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.evendot.toy_shop.model.Product;
import ru.evendot.toy_shop.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Component
public class ProductRepositoryImpl implements ProductRepository {
    private final JdbcTemplate jdbcTemplate;
    private final String SELECT_ALL_FROM_TABLE = "SELECT * FROM PRODUCT";
    public ProductRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public Optional<List<Product>> findAll() {
        return Optional.of(
                jdbcTemplate.query(
                        SELECT_ALL_FROM_TABLE, new BeanPropertyRowMapper<>(Product.class)
                )
        );
    }
}
