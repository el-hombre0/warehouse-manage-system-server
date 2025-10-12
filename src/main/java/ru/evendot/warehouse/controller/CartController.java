package ru.evendot.warehouse.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.evendot.warehouse.exception.ResourceNotFoundException;
import ru.evendot.warehouse.model.Cart;
import ru.evendot.warehouse.model.response.DataResponse;
import ru.evendot.warehouse.service.CartService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")
public class CartController {

    private final CartService cartService;

    /**
     * Получение корзины
     *
     * @param cartId Идентификатор корзины
     * @return Корзина или код ошибки
     */
    @GetMapping("/{cartId}")
    public ResponseEntity<DataResponse> getCart(@PathVariable Long cartId) {
        try {
            Cart cart = cartService.getCart(cartId);
            return ResponseEntity.ok(
                    new DataResponse("Cart received successfully!", cartService.convertToCartDTO(cart)
                    )
            );
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new DataResponse("Cart not found!", e.getMessage())
            );
        }
    }

    /**
     * Очистка корзины
     *
     * @param cartId Идентификатор корзины
     * @return Сообщение, что корзина очищена
     */
    @DeleteMapping("/{cartId}/clear")
    public ResponseEntity<DataResponse> clearCart(@PathVariable Long cartId) {
        try {
            cartService.clearCart(cartId);
            return ResponseEntity.ok().body(new DataResponse("Cart cleared!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DataResponse("Cart with id " +
                    cartId + " not found!", e.getMessage()));
        }
    }

    /**
     * Получение стоимости корзины
     *
     * @param cartId Идентификатор корзины
     * @return Стоимость корзины
     */
    @GetMapping("/{cartId}/cart/total-price")
    public ResponseEntity<DataResponse> getTotalAmount(@PathVariable Long cartId) {
        try {
            Double totalAmount = cartService.getTotalPrice(cartId);
            return ResponseEntity.ok().body(new DataResponse("Total amount received successfully!",
                    totalAmount));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DataResponse("Cart with id " +
                    cartId + "not found!", e.getMessage()));
        }
    }


}
