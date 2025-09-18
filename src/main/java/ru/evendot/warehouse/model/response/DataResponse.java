package ru.evendot.warehouse.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DataResponse {
    private String message;
    private Object object;
}
