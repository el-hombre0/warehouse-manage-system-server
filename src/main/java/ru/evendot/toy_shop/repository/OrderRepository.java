package ru.evendot.toy_shop.repository;

import org.springframework.stereotype.Repository;
import ru.evendot.toy_shop.model.Order;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository {
    Optional<List<Order>> findAll();

//    Optional<Long> save(Order order);

//    Optional<Order> findById(Long id);

//    Boolean existsById(Long id);

//    void deleteById(Long id);

//    Optional<Long> updateById(Order order);
}
