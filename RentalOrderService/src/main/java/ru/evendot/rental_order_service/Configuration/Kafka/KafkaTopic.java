package ru.evendot.rental_order_service.Configuration.Kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopic {
    @Bean
    public NewTopic cartEventsTopic(){
        return TopicBuilder.name("cart-events").build();
    }

    @Bean
    public NewTopic rentalCoreTopic() {
        return TopicBuilder.name("rental-core-events").partitions(3).build();
    }

    @Bean
    public NewTopic rentalStatusEventsTopic(){
        return TopicBuilder.name("rental-status-events").build();
    }

    @Bean
    public NewTopic overdueEventsTopic(){
        return TopicBuilder.name("overdue-events").build();
    }

    @Bean
    public NewTopic rentalExtensionEventsTopic() { return TopicBuilder.name("rental-extension-events").build();}

}
