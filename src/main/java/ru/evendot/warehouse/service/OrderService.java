package ru.evendot.warehouse.service;

import ru.evendot.warehouse.model.Order;

import java.util.List;

public interface OrderService {
    List<Order> getOrders();

    Order getOrder(Long id);

    List<Order> getUserOrders(Long userId);

    Order placeOrder(Long userId);


//    void deleteById(Long id);

//    DataResponseOrder updateOrder(CreateOrder order);
}
