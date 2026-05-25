package ru.evendot.userservice.Broker.Producers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.evendot.userservice.Broker.Events.UserRegisteredEvent;

@Component
public class UserEventsProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUserRegistered(UserRegisteredEvent event){
        kafkaTemplate.send("user-lifcycle-events", event);
    }

}
