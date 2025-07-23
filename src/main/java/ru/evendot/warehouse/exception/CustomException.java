package ru.evendot.warehouse.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException{
    private String code;
    private String message;
}
