package ru.evendot.toy_shop.repository.impl;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.evendot.toy_shop.model.Cart;
import ru.evendot.toy_shop.model.Category;
import ru.evendot.toy_shop.repository.CartRepository;

import java.util.Optional;

@Repository
public class CartRepositoryImpl implements CartRepository {
    private final JdbcTemplate jdbcTemplate;
    public CartRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private final String SELECT_FROM_CARTS_WHERE_ID = "SELECT * FROM carts WHERE id = ?";
    private final String INSERT_INTO_CARTS = "INSERT INTO carts (total_amount, cart_items) VALUES (?, ?)";
    private final String DELETE_BY_ID = "DELETE FROM carts WHERE id = ?";

    @Override
    public Optional<Cart> findById(Long id){
        return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_FROM_CARTS_WHERE_ID, new BeanPropertyRowMapper<>(Cart.class), id));
    }

    @Override
    public Cart save(Cart cart){
        jdbcTemplate.update(INSERT_INTO_CARTS, cart.getTotalAmount(), cart.getCartItems());
        return cart;
    }

    @Override
    public void deleteById(Long id){
        jdbcTemplate.update(DELETE_BY_ID, id);
    }
}
