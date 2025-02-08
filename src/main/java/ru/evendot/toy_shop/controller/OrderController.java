package ru.evendot.toy_shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.evendot.toy_shop.model.response.DataResponse;
import ru.evendot.toy_shop.model.response.order.DataResponseOrderList;
import ru.evendot.toy_shop.service.OrderService;


@RestController
@RequestMapping("/api/v1/order-service")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/order")
    public ResponseEntity<DataResponse> getAllOrders(){
        return ResponseEntity.ok(
                new DataResponse(
                        new DataResponseOrderList(
                                orderService.getOrders()
                        )
                )
        );
    }


}
