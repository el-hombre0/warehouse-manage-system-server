package ru.evendot.toy_shop.repository.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.evendot.toy_shop.model.CartItem;
import ru.evendot.toy_shop.repository.CartItemRepository;

@Repository
public class CartItemRepositoryImpl implements CartItemRepository {
    private final JdbcTemplate jdbcTemplate;
    private final String INSERT_INTO_CART_ITEMS = "INSERT INTO cart_items (quantity, unit_price, total_price, product, cart) VALUES (?, ?, ?, ?, ?)";
    private final String DELETE_CART = "DELETE FROM cart_items WHERE id = ?";
    public CartItemRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public CartItem save(CartItem cartItem) {
        jdbcTemplate.update(INSERT_INTO_CART_ITEMS, cartItem.getQuantity(), cartItem.getUnitPrice(), cartItem.getTotalPrice(), cartItem.getProduct(), cartItem.getCart());
        return cartItem;
    }

    @Override
    public void deleteAllByCartId(Long id) {
        jdbcTemplate.update(DELETE_CART, id);
    }
}
