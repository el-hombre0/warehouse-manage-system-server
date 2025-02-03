package ru.evendot.toy_shop.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateProduct {
    private String title;
    private Long article;
    private String description;
    private Double price;
    private String image;
}
