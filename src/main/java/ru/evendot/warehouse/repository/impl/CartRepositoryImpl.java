//package ru.evendot.warehouse.repository.impl;
//
//import org.springframework.jdbc.core.BeanPropertyRowMapper;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Repository;
//import ru.evendot.warehouse.model.Cart;
//import ru.evendot.warehouse.repository.CartRepository;
//
//import java.util.Optional;
//
//@Repository
//public class CartRepositoryImpl implements CartRepository {
//    private final JdbcTemplate jdbcTemplate;
//    private final String SELECT_FROM_CARTS_WHERE_ID = "SELECT * FROM carts WHERE id = ?";
//    private final String INSERT_INTO_CARTS = "INSERT INTO carts (total_amount) VALUES (?)";
//    private final String DELETE_BY_ID = "DELETE FROM carts WHERE id = ?";
//    private final String UPDATE_CART = "UPDATE carts SET total_amount = ? WHERE id = ?";
//
//    public CartRepositoryImpl(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    @Override
//    public Optional<Cart> findById(Long id) {
//        return Optional.of(jdbcTemplate.queryForObject(SELECT_FROM_CARTS_WHERE_ID, new BeanPropertyRowMapper<>(Cart.class), id));
//    }
//
//    @Override
//    public Cart save(Cart cart) {
//        jdbcTemplate.update(INSERT_INTO_CARTS, cart.getTotalAmount());
//        return cart;
//    }
//
//    @Override
//    public void deleteById(Long id) {
//        jdbcTemplate.update(DELETE_BY_ID, id);
//    }
//
//    @Override
//    public Cart updateCart(Cart cart) {
//        jdbcTemplate.update(UPDATE_CART, cart.getTotalAmount(), cart.getId());
//        return cart;
//    }
//}
