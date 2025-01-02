package ru.evendot.toy_shop.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "toys")
public class Toy {
    @Id
    private Integer id;
    private String title;
    private Integer article;
    private String description;
    private Double price;
    private String image;
}