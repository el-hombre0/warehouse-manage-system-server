package ru.evendot.rental_order_service.Broker.Events.Rental;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.evendot.rental_order_service.Broker.Events.BaseEvent;

@EqualsAndHashCode(callSuper = true)
@Data
public class RentalConfirmedEvent extends BaseEvent {
    private String rentalId;
    private Double totalAmount;

    public RentalConfirmedEvent(Long id, Double totalAmount) {
        this.rentalId = id.toString();
        this.totalAmount = totalAmount;
        setEventType("RENTAL_CONFIRMED");
    }
}
