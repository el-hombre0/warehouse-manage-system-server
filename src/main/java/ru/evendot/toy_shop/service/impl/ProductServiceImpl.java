package ru.evendot.toy_shop.service.impl;

import ru.evendot.toy_shop.model.Product;
import ru.evendot.toy_shop.repository.ProductRepository;
import ru.evendot.toy_shop.repository.impl.ProductRepositoryImpl;

import java.util.List;

public class ProductServiceImpl {
   private ProductRepositoryImpl productRepositoryImpl;

   public List<Product> getProducts(){
       return productRepositoryImpl.findAll().orElseThrow();
   }
}
