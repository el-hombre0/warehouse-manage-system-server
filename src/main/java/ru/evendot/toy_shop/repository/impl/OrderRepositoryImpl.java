package ru.evendot.toy_shop.repository.impl;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.evendot.toy_shop.model.Order;
import ru.evendot.toy_shop.repository.OrderRepository;
import ru.evendot.toy_shop.repository.SQL_Queries;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepositoryImpl implements OrderRepository {
    private final JdbcTemplate jdbcTemplate;

    public OrderRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<List<Order>> findAll() {
        return Optional.of(jdbcTemplate.query(SQL_Queries.SELECT_ALL.toString(), new BeanPropertyRowMapper<>(Order.class)));
    }

//    @Override
//    public Optional<Long> save(Order order) {
//        return Optional.empty();
//    }

//    @Override
//    public Optional<Order> findById(Long id) {
//        return Optional.empty();
//    }

//    @Override
//    public Boolean existsById(Long id) {
//        return null;
//    }

//    @Override
//    public void deleteById(Long id) {
//
//    }

//    @Override
//    public Optional<Long> updateById(Order order) {
//        return Optional.empty();
//    }
}
