package ru.evendot.toy_shop.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import jakarta.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "toys")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private Integer article;
    private String description;
    private Double price;
    private String image;
}