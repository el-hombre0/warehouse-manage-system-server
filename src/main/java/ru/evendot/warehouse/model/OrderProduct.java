package ru.evendot.warehouse.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "order_product")
public class OrderProduct {
    @EmbeddedId
    private OrderProductPK pk;

    public OrderProduct (Order order, Product product) {
        pk = new OrderProductPK();
        pk.setOrder(order);
        pk.setProduct(product);
    }
}
