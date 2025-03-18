package ru.evendot.toy_shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.evendot.toy_shop.exception.ResourceNotFoundException;
import ru.evendot.toy_shop.model.response.DataResponse;
import ru.evendot.toy_shop.service.CartService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")
public class CartController {

    private final CartService cartService;

    @GetMapping("/{cartId}/my-cart")
    public ResponseEntity<DataResponse> getCart(@PathVariable Long cartId) {
        try {
            return ResponseEntity.ok(
                    new DataResponse(
                            cartService.getCart(cartId)
                    )
            );
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404))
                    .body(new DataResponse<>(e.getMessage()));
        }
    }
}
