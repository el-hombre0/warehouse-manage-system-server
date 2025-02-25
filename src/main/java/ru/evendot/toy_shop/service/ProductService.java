package ru.evendot.toy_shop.service;

import ru.evendot.toy_shop.model.Product;
import ru.evendot.toy_shop.model.request.product.CreateProductRequest;
import ru.evendot.toy_shop.model.request.product.UpdateProductRequest;

import java.util.List;
import java.util.Optional;

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
