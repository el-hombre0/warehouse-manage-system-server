package ru.evendot.warehouse.model.request.order;

import lombok.Data;
import ru.evendot.warehouse.dto.OrderProductDto;

import java.util.List;

@Data
public class OrderForm {
    private List<OrderProductDto> productOrders;

}
