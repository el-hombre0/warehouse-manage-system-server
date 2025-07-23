package ru.evendot.warehouse.service;

import ru.evendot.warehouse.model.Product;
import ru.evendot.warehouse.model.request.product.CreateProductRequest;
import ru.evendot.warehouse.model.request.product.UpdateProductRequest;

import java.util.List;

public interface ProductService {
    List<Product> getProducts();

//    List<Product> getProductByCategory(String category);

    Product getProduct(Long article);

    Product getProductById(Long productId);

    Product addProduct(CreateProductRequest product);

    void deleteProductByArticle(Long article);

    void deleteProductById(Long productId);

    Product updateProduct(Long id, UpdateProductRequest product);
}
