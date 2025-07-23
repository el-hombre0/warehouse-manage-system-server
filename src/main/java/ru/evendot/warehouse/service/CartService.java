package ru.evendot.warehouse.service;

import ru.evendot.warehouse.model.Cart;

public interface CartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    Double getTotalPrice(Long id);
    Long initializeNewCart();
}
