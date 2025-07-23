package ru.evendot.warehouse.service;

import ru.evendot.warehouse.exception.CustomException;
import ru.evendot.warehouse.model.Order;
import ru.evendot.warehouse.model.request.order.CreateOrder;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    List<Order> getOrders();

    Order getOrder(UUID id);

    Long save(CreateOrder order) throws CustomException;

//    void deleteById(Long id);

//    DataResponseOrder updateOrder(CreateOrder order);
}
