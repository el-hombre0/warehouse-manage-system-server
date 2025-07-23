package ru.evendot.warehouse.model.response.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.evendot.warehouse.model.Order;

import java.util.List;

@Data
@AllArgsConstructor
public class DataResponseOrderList {
    private List<Order> orders;
}
