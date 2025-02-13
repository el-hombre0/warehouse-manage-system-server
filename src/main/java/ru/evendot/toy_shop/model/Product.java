package ru.evendot.toy_shop.model;

import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Long article;
    private String description;
    private Double price;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> image;

    private Boolean inStock;
    private Integer sale;
    private Timestamp timeInsert;
    private Timestamp timeUpdate;
    private int inventory;
    private Category category;
}