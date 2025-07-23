package ru.evendot.warehouse.exception.Advices;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.evendot.warehouse.exception.ResourceNotFoundException;
import ru.evendot.warehouse.model.response.product.DataErrorResponseProduct;

@ControllerAdvice
public class DefaultAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<DataErrorResponseProduct> handleException(ResourceNotFoundException e){
        DataErrorResponseProduct response = new DataErrorResponseProduct(e.getMessage());
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(404));
    }
}
