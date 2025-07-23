package ru.evendot.warehouse.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String country;
    private Long zipCode;
    private String city;
    private String street;
    private Integer building;
    private Integer corpus;
    private Integer stroeniye;
    private Integer apartment;
}
