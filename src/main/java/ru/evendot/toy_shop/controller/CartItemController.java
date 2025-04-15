package ru.evendot.toy_shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.evendot.toy_shop.exception.ResourceNotFoundException;
import ru.evendot.toy_shop.model.response.DataResponse;
import ru.evendot.toy_shop.service.CartItemService;
import ru.evendot.toy_shop.service.CartService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cartItems")
public class CartItemController {
    private final CartItemService cartItemService;
    private final CartService cartService;

    /**
     * Добавление товара в корзину
     *
     * @param cartId    ID корзины
     * @param productId ID товара
     * @param quantity  Кол-во товаров
     * @return Сообщение о добавлении товара в корзину или об ошибке поиска корзины
     */
    @PostMapping("/add")
    public ResponseEntity<DataResponse> addItemToCart(@RequestParam(required = false) Long cartId, @RequestParam Long productId,
                                                      @RequestParam Integer quantity) {
        try {
            if (cartId == null) { // Если корзина изначально пустая (не существует)
                cartId = cartService.initializeNewCart();
            }
            cartItemService.addItemToCart(cartId, productId, quantity);
            return ResponseEntity.ok(new DataResponse<>("Item added to cart!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(new DataResponse<>(e.getMessage()));
        }
    }

    /**
     * Удаление товара из корзины
     *
     * @param cartId    ID корзины
     * @param productId ID товара
     * @return Сообщение об успешности удаления товара из корзины или сообщение об ошибке поиска корзины
     */
    @DeleteMapping("/remove")
    public ResponseEntity<DataResponse> removeItemFromCart(@RequestParam Long cartId, @RequestParam Long productId) {
        try {
            cartItemService.removeItemFromCart(cartId, productId);
            return ResponseEntity.ok(new DataResponse<>("Item removed from cart!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(new DataResponse<>(e.getMessage()));
        }
    }

    /**
     * Изменение кол-ва единиц товара
     *
     * @param cartId    ID корзины
     * @param productId ID товара
     * @param quantity  Кол-во единиц товара
     * @return Сообщение об успешности изменения кол-ва единиц товара или сообщение об ошибке поиска корзины
     */
    @PatchMapping("/updateQuantity")
    public ResponseEntity<DataResponse> updateItemQuantity(@RequestParam Long cartId,
                                                           @RequestParam Long productId, @RequestParam int quantity) {
        try {
            cartItemService.updateItemQuantity(cartId, productId, quantity);
            return ResponseEntity.ok().body(new DataResponse<>("Item quantity was updated!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(new DataResponse<>(e.getMessage()));
        }
    }


}
