package ru.evendot.toy_shop.model.response.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.evendot.toy_shop.model.Order;

import java.util.List;

@Data
@AllArgsConstructor
public class DataResponseOrderList {
    private List<Order> orders;
}
