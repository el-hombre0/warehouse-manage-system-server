package ru.evendot.warehouse.service.impl;

import com.fasterxml.uuid.Generators;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.evendot.warehouse.exception.CustomException;
import ru.evendot.warehouse.exception.ResourceNotFoundException;
import ru.evendot.warehouse.model.Order;
import ru.evendot.warehouse.model.Statuses;
import ru.evendot.warehouse.model.request.order.CreateOrder;
import ru.evendot.warehouse.repository.impl.OrderRepositoryImpl;
import ru.evendot.warehouse.service.OrderService;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private OrderRepositoryImpl orderRepositoryImpl;
    @Override
    public List<Order> getOrders() {
        return orderRepositoryImpl.findAll().orElseThrow();
    }

    @Override
    public Order getOrder(UUID uuid) {
        return orderRepositoryImpl.findByUUID(uuid).orElseThrow(
                () -> new ResourceNotFoundException("Order with article:" + uuid.toString() + "doesn't exist."));
    }

    @Override
    public Long save(CreateOrder createOrder) throws CustomException {
        Order order = new Order();
        order.setCost(createOrder.getCost());
        order.setUuid(Generators.timeBasedGenerator().generate());
        order.setPayMethod(createOrder.getPayMethod());
        order.setOrderProducts(null);
        order.setUser(createOrder.getUser());
        order.setComment(createOrder.getComment());
        order.setTimeCreation(new Timestamp(System.currentTimeMillis()));
        order.setStatus(Statuses.ACCEPTED);
        order.setAddress(createOrder.getAddress());
        return orderRepositoryImpl.save(order).orElseThrow();
    }

//    @Override
//    public Long save(CreateOrder order) throws CustomException {
//        return 0L;
//    }

//    @Override
//    public void deleteById(Long id) {
//        if (orderRepositoryImpl.existsById(id)) {
//            orderRepositoryImpl.deleteById(id);
//        } else {
//            throw new CustomException("ORDER_DOES_NOT_EXIST", "Заказ не существует");
//        }
//    }

//    @Override
//    public DataResponseOrder updateOrder(CreateOrder order) {
//        return null;
//    }
}
