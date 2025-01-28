package ru.evendot.toy_shop.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.evendot.toy_shop.model.Product;
import ru.evendot.toy_shop.repository.impl.ProductRepositoryImpl;
import ru.evendot.toy_shop.service.ProductService;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
   private ProductRepositoryImpl productRepositoryImpl;

   public List<Product> getProducts(){
       return productRepositoryImpl.findAll().orElseThrow();
   }
}
