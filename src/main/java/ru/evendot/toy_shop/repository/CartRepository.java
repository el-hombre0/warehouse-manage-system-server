package ru.evendot.toy_shop.repository;

import ru.evendot.toy_shop.model.Cart;

import java.util.Optional;

public interface CartRepository {
    Optional<Cart> findById(Long id);

    Cart save(Cart cart);

    void deleteById(Long id);

    Cart updateCart(Cart cart);
}
