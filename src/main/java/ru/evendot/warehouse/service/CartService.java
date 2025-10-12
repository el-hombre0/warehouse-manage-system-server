package ru.evendot.warehouse.service;

import ru.evendot.warehouse.dto.CartDTO;
import ru.evendot.warehouse.model.Cart;
import ru.evendot.warehouse.model.User;

public interface CartService {
    Cart getCart(Long id);

    Cart getCartByUserId(Long userId);

    void clearCart(Long id);

    Double getTotalPrice(Long id);

    Cart initializeNewCart(User user);

    CartDTO convertToCartDTO(Cart cart);
}
