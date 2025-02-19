package ru.evendot.toy_shop.model;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;

    public Product(String title, Long article, String description, Double price, Boolean inStock, Integer sale, Timestamp timeInsert, Timestamp timeUpdate, int inventory, Category category) {
        this.title = title;
        this.article = article;
        this.description = description;
        this.price = price;
        this.inStock = inStock;
        this.sale = sale;
        this.timeInsert = timeInsert;
        this.timeUpdate = timeUpdate;
        this.inventory = inventory;
        this.category = category;
    }
}