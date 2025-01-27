package ru.evendot.toy_shop.model.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DataResponse<T> {
    private T data;
}
