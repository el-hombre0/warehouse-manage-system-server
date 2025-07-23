package ru.evendot.warehouse.model.response.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.evendot.warehouse.model.Order;

@Data
@AllArgsConstructor
public class DataResponseOrderFull {
    private Order order;
}
