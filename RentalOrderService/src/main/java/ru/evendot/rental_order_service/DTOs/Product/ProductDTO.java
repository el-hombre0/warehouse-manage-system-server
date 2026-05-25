package ru.evendot.rental_order_service.DTOs.Product;

import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String title;
    private Long article;
    private String category;
    private String description;
    private Double price;
    private Double hourlyRate;
    private Integer sale;
}
