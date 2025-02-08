package ru.evendot.toy_shop.service;

import ru.evendot.toy_shop.model.Order;

import java.util.List;

public interface OrderService {
    List<Order> getOrders();

//    Order getOrder(Long id);

//    Long save(CreateOrder order) throws CustomException;

//    void deleteById(Long id);

//    DataResponseOrder updateOrder(CreateOrder order);
}
