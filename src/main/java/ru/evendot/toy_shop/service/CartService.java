package ru.evendot.toy_shop.service;

import ru.evendot.toy_shop.model.Cart;

public interface CartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    Double getTotalPrice(Long id);
}
