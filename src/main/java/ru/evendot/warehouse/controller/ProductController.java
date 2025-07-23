package ru.evendot.warehouse.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.evendot.warehouse.exception.ResourceNotFoundException;
import ru.evendot.warehouse.model.request.product.CreateProductRequest;
import ru.evendot.warehouse.model.request.product.UpdateProductRequest;
import ru.evendot.warehouse.model.response.DataResponse;
import ru.evendot.warehouse.model.response.product.DataResponseProductFull;
import ru.evendot.warehouse.model.response.product.DataResponseProductList;
import ru.evendot.warehouse.service.ProductService;

@RestController
@RequestMapping("/api/v1/product-service")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/product")
    public ResponseEntity<DataResponse> getAllProducts() {
        return ResponseEntity.ok(
                new DataResponse(
                        new DataResponseProductList(
                                productService.getProducts()

                        )
                )
        );
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<DataResponse> getProduct(@PathVariable Long id) throws ResourceNotFoundException{
            return ResponseEntity.ok(
                    new DataResponse(
                            new DataResponseProductFull(
                                    productService.getProductById(id)
                            )
                    )
            );
    }

    @PostMapping("/product")
    public ResponseEntity<DataResponse> addProduct(@RequestBody CreateProductRequest product) {
        return ResponseEntity.ok(
                new DataResponse(
                        new DataResponseProductFull(
                                productService.addProduct(product)
                        )
                )

        );
    }

    @DeleteMapping("/product/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<DataResponse> updateProduct(@PathVariable Long id, @RequestBody UpdateProductRequest product ) {
        return ResponseEntity.ok(
                new DataResponse(
                        new DataResponseProductFull(
                                productService.updateProduct(id, product)
                        )
                )
        );
    }
}
