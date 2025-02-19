package ru.evendot.toy_shop.model.request.product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.evendot.toy_shop.model.Category;
import ru.evendot.toy_shop.model.Image;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
public class UpdateProductRequest {
    private Long id;
    private String title;
    private Long article;
    private String description;
    private Double price;
    private Boolean inStock;
    private Integer sale;
    private Timestamp timeUpdate;
    private int inventory;
    private Category category;
}
