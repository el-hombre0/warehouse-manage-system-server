package ru.evendot.warehouse.service.impl;

import com.fasterxml.uuid.Generators;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.evendot.warehouse.dto.OrderDTO;
import ru.evendot.warehouse.exception.ResourceNotFoundException;
import ru.evendot.warehouse.model.*;
import ru.evendot.warehouse.repository.OrderRepository;
import ru.evendot.warehouse.repository.impl.ProductRepositoryImpl;
import ru.evendot.warehouse.service.OrderService;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepositoryImpl productRepository;
    private final CartServiceImpl cartService;
    private final ModelMapper modelMapper;

    @Override
    public List<OrderDTO> getOrders() {
        List<Order> foundOrders = orderRepository.findAll();
        return foundOrders.stream().map(this::convertToOrderDTO).toList();
    }

    @Override
    public OrderDTO getOrder(Long id) {
        return orderRepository.findById(id).map(this::convertToOrderDTO).orElseThrow(
                () -> new ResourceNotFoundException("Order with id:" + id + " doesn't exist."));
    }

    @Override
    public List<OrderDTO> getUserOrders(Long userId) {
        List<Order> orders = orderRepository.findAllByUserId(userId);
        return orders.stream().map(this::convertToOrderDTO).toList();
    }

    /**
     * Размещение заказа (закрепление за пользователем)
     *
     * @param userId ID пользователя
     * @return Заказ
     */
    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItemsList = createOrderItems(order, cart);

        order.setOrderItems(new HashSet<>(orderItemsList));
        order.setTotalAmount(cart.getTotalAmount());
        order.setPaymentMethod(PaymentMethod.NO_PAY);

        //TODO временно
        order.setComment(null);
        order.setOrderStatus(OrderStatus.PENDING);

        // TODO временно
        order.setAddress(null);

        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(cart.getId());
        return savedOrder;
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
        order.setUser(cart.getUser());
//        LocalDate timeNow = LocalDate.now();
//        order.setTimeCreation(Timestamp.valueOf(timeNow.atStartOfDay()));
        order.setUuid(Generators.timeBasedGenerator().generate());
        order.setPaymentStatus(PaymentStatus.PAYMENT_PENDING);

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
            //TODO make an in stock products amount checking
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
//    public void deleteById(Long id) {
//        if (orderRepositoryImpl.existsById(id)) {
//            orderRepositoryImpl.deleteById(id);
//        } else {
//            throw new CustomException("ORDER_DOES_NOT_EXIST", "Заказ не существует");
//        }
//    }

    @Override
    public OrderDTO convertToOrderDTO(Order order) {
        return modelMapper.map(order, OrderDTO.class);
    }
}
