package ru.evendot.warehouse.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.evendot.warehouse.exception.ResourceNotFoundException;
import ru.evendot.warehouse.model.request.product.CreateProductRequest;
import ru.evendot.warehouse.model.request.product.UpdateProductRequest;
import ru.evendot.warehouse.model.response.DataResponse;
import ru.evendot.warehouse.service.ProductService;

@RestController
@RequestMapping("/api/v1/product-service")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/product")
    public ResponseEntity<DataResponse> getAllProducts() {
        return ResponseEntity.ok(
                new DataResponse("Products list received successfully", productService.getProducts()));
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<DataResponse> getProduct(@PathVariable Long id) throws ResourceNotFoundException {
        try {
            return ResponseEntity.ok(
                    new DataResponse("Product received successfully!", productService.getProductById(id)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DataResponse(
                    "Product with id " + id + " not found!", e.getMessage()));
        }
    }

    @PostMapping("/product")
    public ResponseEntity<DataResponse> addProduct(@RequestBody CreateProductRequest product) {
        return ResponseEntity.ok(
                new DataResponse("Product created successfully!", productService.addProduct(product)));
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<DataResponse> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProductById(id);
            return ResponseEntity.ok(new DataResponse(
                    "Product deleted successfully!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DataResponse(
                    "Product with id " + id + " not found!", e.getMessage()));
        }
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<DataResponse> updateProduct(@PathVariable Long id, @RequestBody UpdateProductRequest product) {
        try {
            return ResponseEntity.ok(
                    new DataResponse("Product updated successfully!", productService.updateProduct(id, product)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DataResponse(
                    "Product with id " + id + " not found!", e.getMessage()));
        }
    }
}
