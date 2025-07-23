package ru.evendot.warehouse.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.evendot.warehouse.exception.ResourceNotFoundException;
import ru.evendot.warehouse.model.Cart;
import ru.evendot.warehouse.model.CartItem;
import ru.evendot.warehouse.model.Product;
import ru.evendot.warehouse.repository.impl.CartItemRepositoryImpl;
import ru.evendot.warehouse.repository.impl.CartRepositoryImpl;
import ru.evendot.warehouse.service.CartItemService;
import ru.evendot.warehouse.service.CartService;
import ru.evendot.warehouse.service.ProductService;

import java.util.HashSet;
import java.util.Optional;

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
     *
     * @param cartId    ID корзины
     * @param productId ID продукта
     * @param quantity  количество единиц продукта в корзине
     */
    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProductById(productId);
        CartItem cartItem = Optional.ofNullable(cart.getCartItems()).orElse(new HashSet<>())
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
//        cartRepo.save(cart);
        cartRepo.updateCart(cart);
    }

    /**
     * Удаление товара из корзины
     *
     * @param cartId    ID корзины
     * @param productId ID товара
     */
    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        CartItem itemToRemove = getCartItem(cartId, productId);
        cart.removeItem(itemToRemove);
        cartRepo.save(cart);
    }

    /**
     * Изменение количества единиц товара в корзине
     *
     * @param cartId    ID корзины
     * @param productId ID товара
     */
    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
                });
        Double totalAmount = cart.getCartItems()
                .stream().mapToDouble(CartItem::getTotalPrice).sum();
        cart.setTotalAmount(totalAmount);
        cartRepo.updateCart(cart);
    }

    @Override
    public CartItem getCartItem(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        return cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElseThrow(() -> new ResourceNotFoundException("Item not found!"));
    }
}
