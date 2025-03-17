package ru.evendot.toy_shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.evendot.toy_shop.exception.ResourceNotFoundException;
import ru.evendot.toy_shop.model.Cart;
import ru.evendot.toy_shop.model.CartItem;
import ru.evendot.toy_shop.model.Product;
import ru.evendot.toy_shop.repository.impl.CartItemRepositoryImpl;
import ru.evendot.toy_shop.repository.impl.CartRepositoryImpl;
import ru.evendot.toy_shop.service.CartItemService;
import ru.evendot.toy_shop.service.CartService;
import ru.evendot.toy_shop.service.ProductService;

/**
 * Реализация элемента корзины покупок
 */
@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepositoryImpl cartItemRepo;
    private final CartRepositoryImpl cartRepo;
    private final ProductService productService;
    private final CartService cartService;

    /**
     * Добавление товара в корзину
     * @param cartId ID корзины
     * @param productId ID продукта
     * @param quantity количество единиц продукта в корзине
     */
    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProductById(productId);
        CartItem cartItem = cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElse(new CartItem());

        if (cartItem.getId() == null) {
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
        } else { // Если товар уже лежит в корзине
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartItemRepo.save(cartItem);
        cartRepo.save(cart);
    }

    /**
     * Удаление товара из корзины
     * @param cartId ID корзины
     * @param productId ID товара
     */
    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        CartItem itemToRemove = cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
        cart.removeItem(itemToRemove);
        cartRepo.save(cart);
    }

    /**
     * Изменение количества единиц товара в корзине
     * @param cartId ID корзины
     * @param productId ID товара
     */
    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProductById(productId);
        cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().ifPresent(item -> {
                    item.setQuantity(quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
                });
        Double totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        cartRepo.save(cart);
    }
}
