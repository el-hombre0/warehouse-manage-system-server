package ru.evendot.warehouse.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String country;
    private Long zipCode;
    private String city;
    private String street;
    private Integer building;
    private Integer corpus;
    private Integer stroeniye;
    private Integer apartment;

    @OneToOne(mappedBy = "address")
    private User user;
}
