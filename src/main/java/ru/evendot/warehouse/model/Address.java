package ru.evendot.warehouse.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * TODO
 * Request processing failed: org.springframework.dao.InvalidDataAccessApiUsageException:
 * org.hibernate.TransientObjectException: persistent instance references an unsaved transient
 * instance of 'ru.evendot.warehouse.model.Address' (save the transient instance before flushing)] with root cause
 */
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

    @ManyToMany(mappedBy = "userAddresses")
    private Set<User> users = new HashSet<>();
}
