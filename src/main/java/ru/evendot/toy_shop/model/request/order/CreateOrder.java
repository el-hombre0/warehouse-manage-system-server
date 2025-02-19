package ru.evendot.toy_shop.model.request.order;

import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
import ru.evendot.toy_shop.dto.OrderProductDto;
import ru.evendot.toy_shop.model.*;

import java.sql.Timestamp;
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
