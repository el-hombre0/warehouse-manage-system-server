package ru.evendot.toy_shop.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProduct {
    private String title;
    private Long article;
    private String description;
    private Double price;
    private String image;
    private Boolean inStock;
    private Integer sale;
}
