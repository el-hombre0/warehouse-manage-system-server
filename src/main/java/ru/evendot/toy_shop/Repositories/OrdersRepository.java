package ru.evendot.toy_shop.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.evendot.toy_shop.Models.Order;

@Repository
public interface OrdersRepository extends JpaRepository<Order, Long> {
}
