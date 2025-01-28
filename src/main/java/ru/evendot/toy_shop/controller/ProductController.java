package ru.evendot.toy_shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.evendot.toy_shop.model.response.DataResponseProduct;
import ru.evendot.toy_shop.service.ProductService;

@RestController
@RequestMapping("/api/v1/product-service")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/get-products")
    public ResponseEntity<DataResponseProduct> getAllProducts() {
        return ResponseEntity.ok(
                new DataResponseProduct(
                        productService.getProducts()

                )
        );
    }
}
