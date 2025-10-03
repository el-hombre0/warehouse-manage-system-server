package ru.evendot.warehouse.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ProductDTO {
    private Long id;

    private String title;
    private Long article;
    private String description;
    private Double price;


    private Boolean inStock;
    private Integer sale;
    private Timestamp timeInsert;
    private Timestamp timeUpdate;

    /**
     * Количество товара в наличии
     */
    private int inventory;
}
