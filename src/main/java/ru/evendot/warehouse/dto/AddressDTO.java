package ru.evendot.warehouse.dto;

import lombok.Data;

@Data
public class AddressDTO {
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
