package ru.evendot.rental_order_service.Broker.Producers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.evendot.rental_order_service.Broker.Events.Rental.RentalConfirmedEvent;
import ru.evendot.rental_order_service.Broker.Events.Rental.RentalStartedEvent;

@Component
public class RentalEventProducer {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendRentalConfirmed(RentalConfirmedEvent event){
        kafkaTemplate.send("rental-core-events", event.getRentalId(), event);
    }

    public void sendRentalStarted(RentalStartedEvent event){
        kafkaTemplate.send("rental-core-events", event);
    }
}
