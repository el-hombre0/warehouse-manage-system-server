package ru.evendot.rental_order_service.Broker.Events.Rental;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.evendot.rental_order_service.Broker.Events.BaseEvent;
import ru.evendot.rental_order_service.DTOs.Product.ProductDTO;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class RentalStartedEvent extends BaseEvent {
    private LocalDateTime dateStart;
    private LocalDateTime dateEnd;
    private List<ProductDTO> products;
    private HashMap<Long, Integer> quantities;
    private Long customerId;
    private Long rentalId;
    private Double totalAmount;

    public RentalStartedEvent() {
        setEventType("RENTAL_STARTED");
    }
}
