package ru.evendot.warehouse.service;

import ru.evendot.warehouse.model.Cart;

public interface CartService {
    Cart getCart(Long id);

    Cart getCartByUserId(Long userId);

    void clearCart(Long id);

    Double getTotalPrice(Long id);

    Long initializeNewCart();
}
