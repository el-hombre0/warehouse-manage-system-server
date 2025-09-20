package ru.evendot.warehouse.model.request.order;

import lombok.Data;
import ru.evendot.warehouse.dto.OrderDTO;
import ru.evendot.warehouse.model.*;

import java.util.List;

@Data
public class CreateOrder {
    private Double cost;
    private PaymentMethod paymentMethod;
//    private List<OrderProduct> orderProduct;
    private List<OrderDTO> productOrders;
    private User user;
    private String comment;
    private Address address;
}
