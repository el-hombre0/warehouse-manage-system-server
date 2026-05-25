package ru.evendot.rental_order_service.Services.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.evendot.rental_order_service.Broker.Events.Cart.CartRetrievedEvent;
import ru.evendot.rental_order_service.Broker.Producers.CartEventProducer;
import ru.evendot.rental_order_service.DTOs.CartDTO;
import ru.evendot.rental_order_service.DTOs.User.UserDTO;
import ru.evendot.rental_order_service.Exceptions.ResourceNotFoundException;
import ru.evendot.rental_order_service.Models.Cart;
import ru.evendot.rental_order_service.Models.CartItem;
import ru.evendot.rental_order_service.Repositories.CartItemRepository;
import ru.evendot.rental_order_service.Repositories.CartRepository;
import ru.evendot.rental_order_service.Services.CartService;

import java.util.Optional;

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
    private final ModelMapper modelMapper;

    @Autowired
    private CartEventProducer cartEventProducer;

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
        cartEventProducer.sendCartRetrieved(CartRetrievedEvent.builder()
                .sessionId("")
                .cartId(id)
                .userId(gottenCart.getUserId())
                .totalAmount(gottenCart.getTotalAmount())
                .totalQuantity(gottenCart.getCartItems().stream()
                        .mapToInt(CartItem::getQuantity).sum())
                .status(CartRetrievedEvent.CartStatus.NON_EMPTY)
                .sourceChannel(CartRetrievedEvent.SourceChannel.WEB)
                .build());
        return gottenCart;
//        Double totalAmount = cart.getTotalAmount();
//        cart.setTotalAmount(totalAmount);
//        return cartRepo.updateCart(cart);
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepo.findByUserId(userId);
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
    public Cart initializeNewCart(UserDTO userDTO) {
        return Optional.ofNullable(getCartByUserId(userDTO.getId()))
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUserId(userDTO.getId());
                    return cartRepo.save(cart);
                });
    }

    @Override
    public CartDTO convertToCartDTO(Cart cart){
        return modelMapper.map(cart, CartDTO.class);
    }
}
