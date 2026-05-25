package ru.evendot.rental_order_service.Broker.Events;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public abstract class BaseEvent {
    private String eventId = UUID.randomUUID().toString();
    private LocalDateTime eventTimestamp = LocalDateTime.now();
    private String eventType;
    private int version = 1;

}
