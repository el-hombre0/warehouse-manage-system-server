package ru.evendot.toy_shop.service;

import ru.evendot.toy_shop.exception.CustomException;
import ru.evendot.toy_shop.model.Order;
import ru.evendot.toy_shop.model.request.order.CreateOrder;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    List<Order> getOrders();

    Order getOrder(UUID id);

    Long save(CreateOrder order) throws CustomException;

//    void deleteById(Long id);

//    DataResponseOrder updateOrder(CreateOrder order);
}
