package ru.evendot.toy_shop.model.request.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.evendot.toy_shop.model.Category;
import ru.evendot.toy_shop.model.Image;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequest {
    private String title;
    private Long article;
    private String description;
    private Double price;
    private List<Image> image;
    private Boolean inStock;
    private Integer sale;
    private int inventory;
    private Category category;
}
