package ru.evendot.toy_shop.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.evendot.toy_shop.exception.CustomException;
import ru.evendot.toy_shop.exception.ResourceNotFoundException;
import ru.evendot.toy_shop.model.Order;
import ru.evendot.toy_shop.repository.impl.OrderRepositoryImpl;
import ru.evendot.toy_shop.service.OrderService;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private OrderRepositoryImpl orderRepositoryImpl;
    @Override
    public List<Order> getOrders() {
        return orderRepositoryImpl.findAll().orElseThrow();
    }

//    @Override
//    public Order getOrder(Long id) {
//        return orderRepositoryImpl.findById(id).orElseThrow(
//                () -> new ResourceNotFoundException("Order with article:" + id.toString() + "doesn't exist."));
//    }

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
