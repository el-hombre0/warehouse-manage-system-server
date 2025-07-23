package ru.evendot.warehouse.repository;

import ru.evendot.warehouse.model.CartItem;

public interface CartItemRepository {
    CartItem save(CartItem cartItem);

    void deleteAllByCartId(Long id);
}
