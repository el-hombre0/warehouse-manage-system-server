package ru.evendot.toy_shop.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.evendot.toy_shop.Models.Product;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Long> {
}
