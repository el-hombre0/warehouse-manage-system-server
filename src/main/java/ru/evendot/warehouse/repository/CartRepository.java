package ru.evendot.warehouse.repository;

import ru.evendot.warehouse.model.Cart;

import java.util.Optional;

public interface CartRepository {
    Optional<Cart> findById(Long id);

    Cart save(Cart cart);

    void deleteById(Long id);

    Cart updateCart(Cart cart);
}
