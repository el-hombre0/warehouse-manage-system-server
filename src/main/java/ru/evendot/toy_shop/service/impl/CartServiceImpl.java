package ru.evendot.toy_shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.evendot.toy_shop.exception.ResourceNotFoundException;
import ru.evendot.toy_shop.model.Cart;
import ru.evendot.toy_shop.model.CartItem;
import ru.evendot.toy_shop.repository.CartItemRepository;
import ru.evendot.toy_shop.repository.CartRepository;
import ru.evendot.toy_shop.service.CartService;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;
    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cart with id " + id + "does not exist!")
        );
        Double totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepo.save(cart);
    }

    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cartItemRepo.deleteAllByCartId(id);
        cart.getCartItems().clear();
        cartRepo.deleteById(id);
    }

    @Override
    public Double getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }
}
