package ru.evendot.warehouse.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

//    @OneToMany(mappedBy = "category")
//    private List<Product> products;

    public Category(String name) {
        this.name = name;
    }
}
