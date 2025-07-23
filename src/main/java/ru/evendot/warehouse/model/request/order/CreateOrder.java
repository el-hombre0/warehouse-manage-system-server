package ru.evendot.warehouse.model.request.order;

import lombok.Data;
import ru.evendot.warehouse.dto.OrderProductDto;
import ru.evendot.warehouse.model.*;

import java.util.List;

@Data
public class CreateOrder {
    private Double cost;
    private PayMethods payMethod;
//    private List<OrderProduct> orderProduct;
    private List<OrderProductDto> productOrders;
    private User user;
    private String comment;
    private Address address;
}
