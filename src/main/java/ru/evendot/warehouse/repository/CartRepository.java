package ru.evendot.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.evendot.warehouse.model.Cart;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
//    Optional<Cart> findById(Long id);

//    Cart save(Cart cart);

//    void deleteById(Long id);

//    Cart updateCart(Cart cart);
}
