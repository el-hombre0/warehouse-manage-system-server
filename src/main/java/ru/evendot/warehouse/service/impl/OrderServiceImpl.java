package ru.evendot.warehouse.service.impl;

import com.fasterxml.uuid.Generators;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.evendot.warehouse.exception.CustomException;
import ru.evendot.warehouse.exception.ResourceNotFoundException;
import ru.evendot.warehouse.model.*;
import ru.evendot.warehouse.model.request.order.CreateOrder;
import ru.evendot.warehouse.repository.impl.OrderRepositoryImpl;
import ru.evendot.warehouse.repository.impl.ProductRepositoryImpl;
import ru.evendot.warehouse.service.OrderService;

import java.sql.Timestamp;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepositoryImpl orderRepositoryImpl;
    private final ProductRepositoryImpl productRepository;

    @Override
    public List<Order> getOrders() {
        return orderRepositoryImpl.findAll().orElseThrow();
    }

    @Override
    public Order getOrder(Long id) {
        return orderRepositoryImpl.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Order with id:" + id.toString() + " doesn't exist."));
    }

    @Override
    public Long save(CreateOrder createOrder) throws CustomException {
        Order order = new Order();
        order.setTotalAmount(createOrder.getCost());
        order.setUuid(Generators.timeBasedGenerator().generate());
        order.setPaymentMethod(createOrder.getPaymentMethod());
        order.setPaymentStatus(PaymentStatus.PAYMENT_PENDING);
        order.setOrderProducts(null);
        order.setUser(createOrder.getUser());
        order.setComment(createOrder.getComment());
        order.setTimeCreation(new Timestamp(System.currentTimeMillis()));
        order.setOrderStatus(OrderStatus.PENDING);
        order.setAddress(createOrder.getAddress());
        return orderRepositoryImpl.save(order).orElseThrow();
    }

    /**
     * Размещение заказа (закрепление за пользователем)
     *
     * @param userId ID пользователя
     * @return Заказ
     */
    @Override
    public Order placeOrder(User userId) {
        return null;
    }

    /**
     * Создание заказа
     *
     * @param cart Корзина
     * @return Заказ
     */
    private Order createOrder(Cart cart) {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.PENDING);
        //TODO Добавить установку пользователя
//        LocalDate timeNow = LocalDate.now();
//        order.setTimeCreation(Timestamp.valueOf(timeNow.atStartOfDay()));
        order.setTimeCreation(new Timestamp(System.currentTimeMillis()));
        return order;
    }

    /**
     * Создание списка товаров в заказе, полученных из корзины
     *
     * @param order Заказ
     * @param cart  Корзина
     * @return Список товаров в заказе
     */
    private List<OrderItem> createOrderItems(Order order, Cart cart) {
        return cart.getCartItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            product.setInventory(product.getInventory() - cartItem.getQuantity());
            productRepository.save(product);
            return new OrderItem(order, product, cartItem.getQuantity(), cartItem.getUnitPrice());
        }).toList();
    }

    /**
     * Метод подсчета суммы всех элементов заказа
     *
     * @param orderItems Список элементов заказа
     * @return Сумма заказа
     */
    private Double calculateTotalAmount(List<OrderItem> orderItems) {
        return orderItems.stream().mapToDouble(OrderItem::getPrice).sum();
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
