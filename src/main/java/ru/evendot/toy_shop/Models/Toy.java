package ru.evendot.toy_shop.Models;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class Toy {
    private String title;
    private Integer article;
    private String description;
    private Double price;
    private String image;
}