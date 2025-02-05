package ru.evendot.toy_shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.evendot.toy_shop.model.request.CreateProduct;
import ru.evendot.toy_shop.model.request.DeleteProduct;
import ru.evendot.toy_shop.model.response.DataResponse;
import ru.evendot.toy_shop.model.response.DataResponseProduct;
import ru.evendot.toy_shop.model.response.DataResponseProductList;
import ru.evendot.toy_shop.service.ProductService;

@RestController
@RequestMapping("/api/v1/product-service")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/get-products")
    public ResponseEntity<DataResponse> getAllProducts() {
        return ResponseEntity.ok(
                new DataResponse(
                        new DataResponseProductList(
                                productService.getProducts()

                        )
                )
        );
    }

    @PostMapping("/product")
    public ResponseEntity<DataResponse> addProduct(@RequestBody CreateProduct product) {
        return ResponseEntity.ok(
                new DataResponse(
                        new DataResponseProduct(
                                productService.save(product)
                        )
                )

        );
    }

    @DeleteMapping("/product")
    public void deleteProduct(@RequestBody DeleteProduct product) {
        productService.deleteByArticle(product.getArticle());
    }

    @PutMapping("/product")
    public ResponseEntity<DataResponse> updateProduct(@RequestBody CreateProduct product) {
        return ResponseEntity.ok(
                new DataResponse(
                        new DataResponseProduct(
                                productService.updateProduct(product).getArticle()
                        )
                )
        );
    }
}
