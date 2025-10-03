package ru.evendot.warehouse.dto;

import lombok.Data;

@Data
public class CartItemDTO {
    private Long id;

    /**
     * Кол-во элементов единиц товара в корзине
     */
    private int quantity;
    /**
     * Цена единицы товара в корзине
     */
    private Double unitPrice;

    /**
     * Стоимость всех единиц данного товара в корзине
     */
    private Double totalPrice;

    private ProductDTO product;
}
