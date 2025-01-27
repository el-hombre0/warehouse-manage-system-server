package ru.evendot.toy_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.evendot.toy_shop.model.Order;

@Repository
public interface OrdersRepository extends JpaRepository<Order, Long> {
}
