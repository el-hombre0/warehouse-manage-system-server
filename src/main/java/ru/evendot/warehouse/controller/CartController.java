package ru.evendot.warehouse.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.evendot.warehouse.exception.ResourceNotFoundException;
import ru.evendot.warehouse.model.response.DataResponse;
import ru.evendot.warehouse.model.response.cart.DataResponseCart;
import ru.evendot.warehouse.model.response.cart.DataResponseCartAmount;
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
            return ResponseEntity.ok(
                    new DataResponse<>(
                            cartService.getCart(cartId)
                    )
            );
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404))
                    .body(new DataResponse<>(e.getMessage()));
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
            return ResponseEntity.ok().body(new DataResponse<>(new DataResponseCart("Cart was cleared.")));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(new DataResponse<>(e.getMessage()));
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
            return ResponseEntity.ok().body(new DataResponse<>(new DataResponseCartAmount(totalAmount)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404))
                    .body(new DataResponse<>(e.getMessage()));
        }
    }


}
