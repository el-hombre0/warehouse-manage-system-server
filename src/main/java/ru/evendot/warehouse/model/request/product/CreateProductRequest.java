package ru.evendot.warehouse.model.request.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequest {
    private String title;
    private Long article;
    private String description;
    private Double price;
//    private List<Image> image;
    private Boolean inStock;
    private Integer sale;
    private int inventory;
//    private Category category;
}
