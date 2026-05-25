package ru.evendot.rental_order_service.Broker.Producers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.evendot.rental_order_service.Broker.Events.Cart.CartRetrievedEvent;

@Component
public class CartEventProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendCartRetrieved(CartRetrievedEvent event) {
        kafkaTemplate.send("cart-events", event);
    }
}
