package ru.evendot.toy_shop.repository;

import ru.evendot.toy_shop.model.CartItem;

public interface CartItemRepository {
    CartItem save(CartItem cartItem);

    void deleteAllByCartId(Long id);
}
