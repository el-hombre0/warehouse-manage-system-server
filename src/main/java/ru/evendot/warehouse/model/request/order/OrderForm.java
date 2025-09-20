package ru.evendot.warehouse.model.request.order;

import lombok.Data;
import ru.evendot.warehouse.dto.OrderDTO;

import java.util.List;

@Data
public class OrderForm {
    private List<OrderDTO> productOrders;

}
