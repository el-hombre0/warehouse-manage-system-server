package ru.evendot.toy_shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.evendot.toy_shop.model.request.CreateProduct;
import ru.evendot.toy_shop.model.response.DataResponseProduct;
import ru.evendot.toy_shop.model.response.DataResponseProductList;
import ru.evendot.toy_shop.service.ProductService;

@RestController
@RequestMapping("/api/v1/product-service")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/get-products")
    public ResponseEntity<DataResponseProductList> getAllProducts() {
        return ResponseEntity.ok(
                new DataResponseProductList(
                        productService.getProducts()

                )
        );
    }

    @PostMapping("/product")
    public ResponseEntity<DataResponseProduct> addProduct(@RequestBody CreateProduct product) {
        return ResponseEntity.ok(
                new DataResponseProduct(
                        productService.save(product)
                )
        );
    }
}
