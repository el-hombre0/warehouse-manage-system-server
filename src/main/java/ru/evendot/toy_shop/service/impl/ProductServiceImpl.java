package ru.evendot.toy_shop.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.evendot.toy_shop.exception.CustomException;
import ru.evendot.toy_shop.model.Product;
import ru.evendot.toy_shop.model.request.CreateProduct;
import ru.evendot.toy_shop.repository.impl.ProductRepositoryImpl;
import ru.evendot.toy_shop.service.ProductService;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private ProductRepositoryImpl productRepositoryImpl;

    public List<Product> getProducts() {
        return productRepositoryImpl.findAll().orElseThrow();
    }

    public Long save(CreateProduct createProduct) {
        if (!productRepositoryImpl.existsByArticle(createProduct.getArticle())) {
            Product product = new Product();
            product.setArticle(createProduct.getArticle());
            product.setDescription(createProduct.getDescription());
            product.setTitle(createProduct.getTitle());
            product.setPrice(createProduct.getPrice());
            product.setImage(createProduct.getImage());
            return productRepositoryImpl.save(product).orElseThrow();
        } else {
            throw new CustomException("PRODUCT_ALREADY_EXISTS", "Продукт уже существует");
        }
    }
}
