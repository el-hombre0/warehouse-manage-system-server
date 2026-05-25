package ru.evendot.userservice.Configuration.Kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopic {
    @Bean
    public NewTopic userLifecycleEventsTopic(){
        return TopicBuilder.name("user-lifcycle-events").build();
    }

    @Bean
    public NewTopic userProfileEventsTopic() {
        return TopicBuilder.name("user-profile-events").partitions(3).build();
    }

    @Bean
    public NewTopic userAuthenticationEventsTopic() {
        return TopicBuilder.name("user-authentication-events").partitions(3).build();
    }

    @Bean
    public NewTopic userPasswordEventsTopic(){
        return TopicBuilder.name("user-password-events").build();
    }

    @Bean
    public NewTopic paymentMethodsEventsTopic(){
        return TopicBuilder.name("payment-methods-events").build();
    }
}
