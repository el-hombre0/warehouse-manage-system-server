package ru.evendot.toy_shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.evendot.toy_shop.exception.ResourceNotFoundException;
import ru.evendot.toy_shop.model.Cart;
import ru.evendot.toy_shop.repository.CartItemRepository;
import ru.evendot.toy_shop.repository.impl.CartRepositoryImpl;
import ru.evendot.toy_shop.service.CartService;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Сервис работы с корзиной покупок
 */
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepositoryImpl cartRepo;
    private final CartItemRepository cartItemRepo;
    private final AtomicLong cartIdGenerator = new AtomicLong(0);

    /**
     * Получение корзины
     *
     * @param id ID корзины
     * @return Корзина
     */
    @Override
    public Cart getCart(Long id) {
        return cartRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Cart with id " + id + "does not exist!")
        );
//        Double totalAmount = cart.getTotalAmount();
//        cart.setTotalAmount(totalAmount);
//        return cartRepo.updateCart(cart);
    }

    /**
     * Очистка корзины
     *
     * @param id ID корзины
     */
    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cartItemRepo.deleteAllByCartId(id);
        cart.getCartItems().clear();
        cartRepo.deleteById(id);
    }

    /**
     * Получение стоимости товаров в корзине
     *
     * @param id ID корзины
     * @return Стоимость товаров в корзине
     */
    @Override
    public Double getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }

    @Override
    public Long initializeNewCart() {
        Cart cart = new Cart();
        Long newCartId = cartIdGenerator.incrementAndGet();
        cart.setId(newCartId);
        return cartRepo.save(cart).getId();
    }
}
