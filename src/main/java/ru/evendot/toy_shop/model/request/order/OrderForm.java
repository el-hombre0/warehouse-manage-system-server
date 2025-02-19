package ru.evendot.toy_shop.model.request.order;

import lombok.Data;
import ru.evendot.toy_shop.dto.OrderProductDto;

import java.util.List;

@Data
public class OrderForm {
    private List<OrderProductDto> productOrders;

}
