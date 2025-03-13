package ru.evendot.toy_shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.evendot.toy_shop.model.Cart;
import ru.evendot.toy_shop.model.CartItem;
import ru.evendot.toy_shop.model.Product;
import ru.evendot.toy_shop.repository.impl.CartItemRepositoryImpl;
import ru.evendot.toy_shop.repository.impl.CartRepositoryImpl;
import ru.evendot.toy_shop.service.CartItemService;
import ru.evendot.toy_shop.service.CartService;
import ru.evendot.toy_shop.service.ProductService;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepositoryImpl cartItemRepo;
    private final CartRepositoryImpl cartRepo;
    private final ProductService productService;
    private final CartService cartService;

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

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {

    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId) {

    }
}
