package ru.evendot.toy_shop.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.evendot.toy_shop.exception.CustomException;
import ru.evendot.toy_shop.exception.ResourceNotFoundException;
import ru.evendot.toy_shop.model.Product;
import ru.evendot.toy_shop.model.request.CreateProduct;
import ru.evendot.toy_shop.model.response.DataResponseProduct;
import ru.evendot.toy_shop.repository.impl.ProductRepositoryImpl;
import ru.evendot.toy_shop.service.ProductService;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private ProductRepositoryImpl productRepositoryImpl;

    public List<Product> getProducts() {
        return productRepositoryImpl.findAll().orElseThrow();
    }

    public Product getProduct(Long article) {
        return productRepositoryImpl.findByArticle(article).orElseThrow(
                () -> new ResourceNotFoundException("Product with article:" + article.toString() + "doesn't exist."));
    }

    public Long save(CreateProduct createProduct) {
        if (!productRepositoryImpl.existsByArticle(createProduct.getArticle())) {
            Product product = new Product();
            product.setArticle(createProduct.getArticle());
            product.setDescription(createProduct.getDescription());
            product.setTitle(createProduct.getTitle());
            product.setPrice(createProduct.getPrice());
            product.setImage(createProduct.getImage());
            product.setInStock(createProduct.getInStock());
            product.setSale(createProduct.getSale());
            product.setTimeInsert(new Timestamp(System.currentTimeMillis()));
            product.setTimeUpdate(product.getTimeInsert());
            return productRepositoryImpl.save(product).orElseThrow();
        } else {
            throw new CustomException("PRODUCT_ALREADY_EXISTS", "Продукт уже существует");
        }
    }

    public void deleteByArticle(Long article) {
        if (productRepositoryImpl.existsByArticle(article)) {
            productRepositoryImpl.deleteByArticle(article);
        } else {
            throw new CustomException("PRODUCT_DOES_NOT_EXIST", "Продукт не существует");
        }
    }

    public DataResponseProduct updateProduct(CreateProduct product) {
        Optional<Product> optionalProduct = productRepositoryImpl.findByArticle(product.getArticle());
        Product putProduct = optionalProduct.orElseThrow(() -> new ResourceNotFoundException("Product with article:" + product.getArticle().toString() + "doesn't exist."));
        putProduct.setArticle(product.getArticle());
        putProduct.setDescription(product.getDescription());
        putProduct.setTitle(product.getTitle());
        putProduct.setPrice(product.getPrice());
        putProduct.setImage(product.getImage());
        putProduct.setInStock(product.getInStock());
        putProduct.setSale(product.getSale());
        putProduct.setTimeUpdate(new Timestamp(System.currentTimeMillis()));
        productRepositoryImpl.updateByArticle(putProduct);
        return new DataResponseProduct(product.getArticle());
    }
}
