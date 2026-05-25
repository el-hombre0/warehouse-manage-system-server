package ru.evendot.rental_order_service.Broker.Producers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.evendot.rental_order_service.Broker.Events.Cart.ItemAddedToCartEvent;
import ru.evendot.rental_order_service.Broker.Events.Cart.ItemQuantityUpdatedEvent;
import ru.evendot.rental_order_service.Broker.Events.Cart.ItemRemovedFromCartEvent;

@Component
public class CartItemEventProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendItemAddedToCart(ItemAddedToCartEvent event){
        kafkaTemplate.send("cart-events", event);
    }

    public void sendItemRemovedFromCart(ItemRemovedFromCartEvent event) {
        kafkaTemplate.send("cart-events", event);
    }

    public void sendItemQuantityUpdatedEvent(ItemQuantityUpdatedEvent event) {
        kafkaTemplate.send("cart-events", event);
    }
}
