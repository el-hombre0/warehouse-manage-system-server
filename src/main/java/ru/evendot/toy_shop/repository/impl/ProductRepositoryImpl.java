package ru.evendot.toy_shop.repository.impl;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.evendot.toy_shop.model.Product;
import ru.evendot.toy_shop.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
    private final JdbcTemplate jdbcTemplate;
    private final String SELECT_ALL_FROM_TABLE = "SELECT * FROM products";
    private final String INSERT_INTO_TABLE = "INSERT INTO products (title, article, description, price, in_stock, sale, time_insert, time_update, inventory) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String SELECT_FROM_TABLE_WHERE_ARTICLE = "SELECT * FROM products WHERE article = ?";
    private final String SELECT_EXISTS_FROM_TABLE_WHERE_ARTICLE = "SELECT EXISTS(SELECT * FROM products WHERE article = ? )";
    private final String DELETE_BY_ARTICLE = "DELETE FROM products WHERE article = ?";
    private final String UPDATE_TABLE_WHERE_ARTICLE = "UPDATE products SET title = ?, description = ?, price = ?, in_stock = ?, sale = ?, time_update = ?, inventory = ? WHERE article = ?";
    private final String UPDATE_TABLE_WHERE_ID = "UPDATE products SET title = ?, description = ?, price = ?, in_stock = ?, sale = ?, time_update = ?, article = ?, inventory = ? WHERE id = ?";
    private final String SELECT_FROM_TABLE_WHERE_ID = "SELECT * FROM products WHERE id = ?";
    private final String DELETE_BY_ID = "DELETE FROM products WHERE id = ?";
    private final String SELECT_FROM_PRODUCTS_WHERE_CATEGORY = "SELECT * FROM products WHERE category = ?";

    public ProductRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query(SELECT_ALL_FROM_TABLE, new BeanPropertyRowMapper<>(Product.class));
    }

    public Optional <Product> save(Product product) {
        jdbcTemplate.update(INSERT_INTO_TABLE,
                product.getTitle(),
                product.getArticle(),
                product.getDescription(),
                product.getPrice(),
//                product.getImage(),
                product.getInStock(),
                product.getSale(),
                product.getTimeInsert(),
                product.getTimeUpdate(),
                product.getInventory());
        return Optional.of(product);
    }

    public Optional<Product> findByArticle(Long article) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                SELECT_FROM_TABLE_WHERE_ARTICLE,
                new BeanPropertyRowMapper<>(Product.class),
                article)
        );
    }

    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                SELECT_FROM_TABLE_WHERE_ID,
                new BeanPropertyRowMapper<>(Product.class),
                id)
        );
    }

//    public List<Product> findByCategory(String category) {
//        return jdbcTemplate.query(SELECT_FROM_PRODUCTS_WHERE_CATEGORY, new BeanPropertyRowMapper<>(Product.class), category);
//    }

    public Boolean existsByArticle(Long article) {
        return jdbcTemplate.queryForObject(SELECT_EXISTS_FROM_TABLE_WHERE_ARTICLE, Boolean.class, article);
    }

    @Override
    public void deleteByArticle(Long article) {
        jdbcTemplate.update(DELETE_BY_ARTICLE, article);
    }

    @Override
    public void deleteById(Product product) {
        jdbcTemplate.update(DELETE_BY_ID, product.getId());
    }

    @Override
    public Optional<Long> updateByArticle(Product product) {
        jdbcTemplate.update(UPDATE_TABLE_WHERE_ARTICLE,
                product.getTitle(),
                product.getDescription(),
                product.getPrice(),
//                product.getImage(),
                product.getInStock(),
                product.getSale(),
                product.getTimeUpdate(),
                product.getArticle(),
                product.getInventory());
        return Optional.of(product.getArticle());
    }

    @Override
    public Optional<Product> updateById(Long id, Product product) {
        jdbcTemplate.update(UPDATE_TABLE_WHERE_ID,
                product.getTitle(),
                product.getDescription(),
                product.getPrice(),
//                product.getImage(),
                product.getInStock(),
                product.getSale(),
                product.getTimeUpdate(),
                product.getArticle(),
                product.getInventory(),
                id);
        return Optional.of(product);
    }

    public static class CategoryRepositoryImpl {
    }
}
