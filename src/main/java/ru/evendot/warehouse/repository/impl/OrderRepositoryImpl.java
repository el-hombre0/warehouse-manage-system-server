package ru.evendot.warehouse.repository.impl;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.evendot.warehouse.model.Order;
import ru.evendot.warehouse.repository.OrderRepository;
import ru.evendot.warehouse.repository.SQL_Queries;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @Override
    public Optional<Long> save(Order order) {
        jdbcTemplate.update(SQL_Queries.INSERT_INTO_TABLE.toString(),
                order.getCost(),
                order.getUuid(),
                order.getPayMethod(),
                order.getOrderProducts(),
                order.getUser(),
                order.getComment(),
                order.getTimeCreation(),
                order.getStatus(),
                order.getAddress());
        return Optional.of(order.getId());
    }

    @Override
    public Optional<Order> findByUUID(UUID uuid) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                SQL_Queries.SELECT_FROM_TABLE_WHERE_ID.toString(),
                new BeanPropertyRowMapper<>(Order.class),
                uuid)
        );
    }

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
