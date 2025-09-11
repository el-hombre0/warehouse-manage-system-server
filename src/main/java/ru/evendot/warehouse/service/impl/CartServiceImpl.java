package ru.evendot.warehouse.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.evendot.warehouse.exception.ResourceNotFoundException;
import ru.evendot.warehouse.model.Cart;
import ru.evendot.warehouse.repository.CartItemRepository;
import ru.evendot.warehouse.repository.CartRepository;
import ru.evendot.warehouse.service.CartService;

/**
 * Сервис работы с корзиной покупок
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    //    private final CartRepositoryImpl cartRepo;
    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;
//    private final AtomicLong cartIdGenerator = new AtomicLong(0);

    /**
     * Получение корзины
     *
     * @param id ID корзины
     * @return Корзина
     */
    @Override
    public Cart getCart(Long id) {
        log.info("Received request to get cart");
        Cart gottenCart = cartRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Cart with id " + id + " does not exist!")
        );
        log.info("Retrieved cart with id: {}, totalAmount: {}", id, gottenCart.getTotalAmount());
        return gottenCart;
//        Double totalAmount = cart.getTotalAmount();
//        cart.setTotalAmount(totalAmount);
//        return cartRepo.updateCart(cart);
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        // TODO
        return null;
    }

    /**
     * Очистка корзины
     *
     * @param id ID корзины
     */
    //TODO Изменить response data message на data
    @Transactional
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
//        Long newCartId = cartIdGenerator.incrementAndGet();
//        cart.setId(newCartId);
        return cartRepo.save(cart).getId();
    }
}
