package ru.evendot.rental_order_service.Broker.Consumers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.evendot.rental_order_service.Services.Impl.CartServiceImpl;

@Component
public class CartEventConsumer {
    @Autowired
    private CartServiceImpl cartService;



}
