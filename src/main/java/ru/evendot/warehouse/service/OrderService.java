package ru.evendot.warehouse.service;

import ru.evendot.warehouse.exception.CustomException;
import ru.evendot.warehouse.model.Order;
import ru.evendot.warehouse.model.User;
import ru.evendot.warehouse.model.request.order.CreateOrder;

import java.util.List;

public interface OrderService {
    List<Order> getOrders();

    Order getOrder(Long id);

    Long save(CreateOrder order) throws CustomException;

    Order placeOrder(User userId);

//    void deleteById(Long id);

//    DataResponseOrder updateOrder(CreateOrder order);
}
