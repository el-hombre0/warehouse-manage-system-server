package ru.evendot.toy_shop.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DataResponse<T> {
    private T data;
}
