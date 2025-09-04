package ru.evendot.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.evendot.warehouse.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
//    CartItem save(CartItem cartItem);

    void deleteAllByCartId(Long id);
}
