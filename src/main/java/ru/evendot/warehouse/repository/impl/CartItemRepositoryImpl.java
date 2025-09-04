//package ru.evendot.warehouse.repository.impl;
//
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Repository;
//import ru.evendot.warehouse.model.CartItem;
//import ru.evendot.warehouse.repository.CartItemRepository;
//
//@Repository
//public class CartItemRepositoryImpl implements CartItemRepository {
//    private final JdbcTemplate jdbcTemplate;
//    private final String INSERT_INTO_CART_ITEMS = "INSERT INTO cart_items (quantity, unit_price, total_price, product_id, cart_id) VALUES (?, ?, ?, ?, ?)";
//    private final String DELETE_CART = "DELETE FROM cart_items WHERE id = ?";
//
//    public CartItemRepositoryImpl(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    @Override
//    public CartItem save(CartItem cartItem) {
//        jdbcTemplate.update(INSERT_INTO_CART_ITEMS, cartItem.getQuantity(), cartItem.getUnitPrice(), cartItem.getTotalPrice(), cartItem.getProduct().getId(), cartItem.getCart().getId());
//        return cartItem;
//    }
//
//    @Override
//    public void deleteAllByCartId(Long id) {
//        jdbcTemplate.update(DELETE_CART, id);
//    }
//}
