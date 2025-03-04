package ru.evendot.toy_shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.evendot.toy_shop.exception.ResourceNotFoundException;
import ru.evendot.toy_shop.model.Cart;
import ru.evendot.toy_shop.repository.CartRepository;
import ru.evendot.toy_shop.service.CartService;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepo;

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

    }

    @Override
    public Double getTotalPrice(Long id) {
        return 0.0;
    }
}
