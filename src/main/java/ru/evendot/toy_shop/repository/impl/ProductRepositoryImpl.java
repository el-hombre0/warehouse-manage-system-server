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
    private final String SELECT_ALL_FROM_TABLE = "SELECT * FROM products";
    private final String INSERT_INTO_TABLE = "INSERT INTO products (title, article, description, price, image, in_stock, sale, time_insert, time_update) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String SELECT_FROM_TABLE_WHERE_ARTICLE = "SELECT * FROM products WHERE article = ?";
    private final String SELECT_EXISTS_FROM_TABLE_WHERE_ARTICLE = "SELECT EXISTS(SELECT * FROM products WHERE article = ? )";
    private final String DELETE_BY_ARTICLE = "DELETE FROM products WHERE article = ?";
    private final String UPDATE_TABLE_WHERE_ARTICLE = "UPDATE products SET title = ?, description = ?, price = ?, image = ?, in_stock = ?, sale = ?, time_insert = ?, time_update = ? WHERE article = ?";

    public ProductRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<List<Product>> findAll() {
        return Optional.of(jdbcTemplate.query(SELECT_ALL_FROM_TABLE, new BeanPropertyRowMapper<>(Product.class)));
    }

    public Optional<Long> save(Product product) {
        jdbcTemplate.update(INSERT_INTO_TABLE,
                product.getTitle(),
                product.getArticle(),
                product.getDescription(),
                product.getPrice(),
                product.getImage(),
                product.getInStock(),
                product.getSale(),
                product.getTimeInsert(),
                product.getTimeUpdate());
        return Optional.of(product.getArticle());
    }

    public Optional<Product> findByArticle(Long article) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                SELECT_FROM_TABLE_WHERE_ARTICLE,
                new BeanPropertyRowMapper<>(Product.class),
                article)
        );
    }

    public Boolean existsByArticle(Long article) {
        return jdbcTemplate.queryForObject(SELECT_EXISTS_FROM_TABLE_WHERE_ARTICLE, Boolean.class, article);
    }

    @Override
    public void deleteByArticle(Long article) {
        jdbcTemplate.update(DELETE_BY_ARTICLE, article);
    }

    @Override
    public Optional<Long> updateByArticle(Product product) {
        jdbcTemplate.update(UPDATE_TABLE_WHERE_ARTICLE,
                product.getTitle(),
                product.getDescription(),
                product.getPrice(),
                product.getImage(),
                product.getInStock(),
                product.getSale(),
                product.getTimeInsert(),
                product.getTimeUpdate(),
                product.getArticle());
        return Optional.of(product.getArticle());
    }
}
