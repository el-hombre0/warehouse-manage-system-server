package ru.evendot.toy_shop.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.evendot.toy_shop.exception.CustomException;
import ru.evendot.toy_shop.exception.ResourceNotFoundException;
import ru.evendot.toy_shop.model.Category;
import ru.evendot.toy_shop.model.Product;
import ru.evendot.toy_shop.model.request.product.CreateProductRequest;
import ru.evendot.toy_shop.model.request.product.UpdateProductRequest;
import ru.evendot.toy_shop.repository.impl.CategoryRepositoryImpl;
import ru.evendot.toy_shop.repository.impl.ProductRepositoryImpl;
import ru.evendot.toy_shop.service.ProductService;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private ProductRepositoryImpl productRepositoryImpl;
    private CategoryRepositoryImpl categoryRepositoryImpl;

    public List<Product> getProducts() {
        return productRepositoryImpl.findAll();
    }

//    @Override
//    public List<Product> getProductByCategory(String category) {
//        return productRepositoryImpl.findByCategory(category);
//    }

    public Product getProduct(Long article) {
        return productRepositoryImpl.findByArticle(article).orElseThrow(
                () -> new ResourceNotFoundException("Product with article:" + article.toString() + "doesn't exist."));
    }

    @Override
    public Product getProductById(Long id) {
        return productRepositoryImpl.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found."));
    }

    @Override
    public Product addProduct(CreateProductRequest request) {
        Product addedProduct;
        addedProduct = createProduct(request);
        productRepositoryImpl.save(addedProduct);
        return productRepositoryImpl.findByArticle(request.getArticle()).orElseThrow(() -> new ResourceNotFoundException("Product not found."));
    }
//    @Override
//    public Product addProduct(CreateProductRequest request) {
//        Product addedProduct;
//        if(categoryRepositoryImpl.findByName(request.getCategory().getName()).isEmpty()){
//            Category newCategory = new Category(request.getCategory().getName());
//            categoryRepositoryImpl.save(newCategory);
//            addedProduct = createProduct(request, newCategory);
//            productRepositoryImpl.save(addedProduct);
//        }
//        else {
//            addedProduct = createProduct(request, request.getCategory());
//            productRepositoryImpl.save(addedProduct);
//        }
//        return addedProduct;

//        Category category = Optional.ofNullable(categoryRepositoryImpl.findByName(request.getCategory().getName())).orElseGet(()->{
//            Category newCategory = new Category(request.getCategory().getName());
//            return categoryRepositoryImpl.save(newCategory);
//        });
//        request.setCategory(category);
//        return productRepositoryImpl.save(createProduct(request, category));
//            if(productRepositoryImpl.findByArticle(request.getArticle()).isPresent()){
//                throw new ResourceAlreadyExistsException("Product already exists");
//            }
//            else{
//                Timestamp creationTime = new Timestamp(System.currentTimeMillis());
//                return new Product(
//                        request.getTitle(),
//                        request.getArticle(),
//                        request.getDescription(),
//                        request.getPrice(),
//                        request.getInStock(),
//                        request.getSale(),
//                        creationTime,
//                        creationTime,
//                        request.getInventory(),
//                        request.getCategory()
//                );
//            }
//    }

//    private Product createProduct(CreateProductRequest request, Category category){
//        Timestamp creationTime = new Timestamp(System.currentTimeMillis());
//        return new Product(
//                request.getTitle(),
//                request.getArticle(),
//                request.getDescription(),
//                request.getPrice(),
//                request.getInStock(),
//                request.getSale(),
//                creationTime,
//                creationTime,
//                request.getInventory(),
//                category
//        );
//    }
    private Product createProduct(CreateProductRequest request){
        Timestamp creationTime = new Timestamp(System.currentTimeMillis());
        return new Product(
                request.getTitle(),
                request.getArticle(),
                request.getDescription(),
                request.getPrice(),
                request.getInStock(),
                request.getSale(),
                creationTime,
                creationTime,
                request.getInventory()
        );
    }

    @Override
    public void deleteProductByArticle(Long article) {
        if (productRepositoryImpl.existsByArticle(article)) {
            productRepositoryImpl.deleteByArticle(article);
        } else {
            throw new CustomException("PRODUCT_DOES_NOT_EXIST", "Продукт не существует");
        }
    }

    @Override
    public void deleteProductById(Long id){
        Optional<Product> optionalProduct = productRepositoryImpl.findById(id);
        Product existingProduct = optionalProduct.orElseThrow(
                () -> new ResourceNotFoundException("Product with id:" + id + "doesn't exist."));
        productRepositoryImpl.deleteById(existingProduct);
//        productRepositoryImpl.findById(id).ifPresentOrElse(productRepositoryImpl :: deleteById,
//                () -> { throw new ResourceNotFoundException("Product not found.");});
    }

    @Override
    public Product updateProduct(Long id, UpdateProductRequest request) {
//        return productRepositoryImpl.findById(request.getId())
//                .map(existingProduct -> updateExistingProduct(existingProduct, request))
//                .map(productRepositoryImpl :: save)
//                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            Optional<Product> optionalProduct = productRepositoryImpl.findById(id);
            Product existingProduct = optionalProduct.orElseThrow(
                    () -> new ResourceNotFoundException("Product with id:" + id + "doesn't exist."));
            Product savedProduct = updateExistingProduct(existingProduct, request);
            productRepositoryImpl.updateById(id, savedProduct);
            return savedProduct;
//            putProduct.setArticle(product.getArticle());
//            putProduct.setDescription(product.getDescription());
//            putProduct.setTitle(product.getTitle());
//            putProduct.setPrice(product.getPrice());
//            putProduct.setImage(product.getImage());
//            putProduct.setInStock(product.getInStock());
//            putProduct.setSale(product.getSale());
//            putProduct.setTimeUpdate(new Timestamp(System.currentTimeMillis()));
//            productRepositoryImpl.updateByArticle(putProduct);
//            return new DataResponseProductFull(product);

    }

//    private Product updateExistingProduct(Product existingProduct, UpdateProductRequest request){
//        existingProduct.setTitle(request.getTitle());
//        existingProduct.setArticle(request.getArticle());
//        existingProduct.setDescription(request.getDescription());
//        existingProduct.setPrice(request.getPrice());
//        existingProduct.setInStock(request.getInStock());
//        existingProduct.setSale(request.getSale());
//        existingProduct.setTimeUpdate(new Timestamp(System.currentTimeMillis()));
//        existingProduct.setInventory(request.getInventory());
//        if(categoryRepositoryImpl.findByName(request.getCategory().getName()).isEmpty()){
//            Category newCategory = new Category(request.getCategory().getName());
//            categoryRepositoryImpl.save(newCategory);
//            existingProduct.setCategory(newCategory);
//        }
//        existingProduct.setCategory(request.getCategory());
//        return existingProduct;
//    }
    private Product updateExistingProduct(Product existingProduct, UpdateProductRequest request){
        existingProduct.setTitle(request.getTitle());
        existingProduct.setArticle(request.getArticle());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInStock(request.getInStock());
        existingProduct.setSale(request.getSale());
        existingProduct.setTimeUpdate(new Timestamp(System.currentTimeMillis()));
        existingProduct.setInventory(request.getInventory());
        return existingProduct;
    }
}
