package ru.evendot.toy_shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.evendot.toy_shop.model.response.DataResponse;
import ru.evendot.toy_shop.model.response.DataResponseProduct;
import ru.evendot.toy_shop.repository.ProductRepository;
import ru.evendot.toy_shop.service.ProductService;

@RestController
@RequestMapping("/api/v1/product-service")
public class ProductController {

    @Autowired
    private ProductRepository productsRepo;
    private ProductService productService;

    @GetMapping("/get-products")
    public ResponseEntity<DataResponse> getAllProducts() {
        return ResponseEntity.ok(
                new DataResponse(
                        new DataResponseProduct(
                                productService.getProducts()
                        )
                )
        );
    }
}
