package ru.evendot.warehouse.repository;

import org.springframework.stereotype.Repository;
import ru.evendot.warehouse.model.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository {
    Optional<List<Order>> findAll();

    Order save(Order order);

    Optional<Order> findByUUID(UUID uuid);

    Optional<Order> findById(Long id);

//    Boolean existsById(Long id);

//    void deleteById(Long id);

//    Optional<Long> updateById(Order order);
}
