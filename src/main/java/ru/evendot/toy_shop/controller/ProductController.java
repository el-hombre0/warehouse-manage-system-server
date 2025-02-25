package ru.evendot.toy_shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.evendot.toy_shop.model.request.product.CreateProductRequest;
import ru.evendot.toy_shop.model.request.product.DeleteProduct;
import ru.evendot.toy_shop.model.request.product.UpdateProductRequest;
import ru.evendot.toy_shop.model.response.DataResponse;
import ru.evendot.toy_shop.model.response.product.DataResponseProduct;
import ru.evendot.toy_shop.model.response.product.DataResponseProductFull;
import ru.evendot.toy_shop.model.response.product.DataResponseProductList;
import ru.evendot.toy_shop.service.ProductService;

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
    public ResponseEntity<DataResponse> getProduct(@PathVariable Long id){
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
