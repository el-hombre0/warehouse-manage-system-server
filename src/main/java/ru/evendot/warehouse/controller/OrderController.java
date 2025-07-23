package ru.evendot.warehouse.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.evendot.warehouse.model.request.order.CreateOrder;
import ru.evendot.warehouse.model.response.DataResponse;
import ru.evendot.warehouse.model.response.order.DataResponseOrder;
import ru.evendot.warehouse.model.response.order.DataResponseOrderFull;
import ru.evendot.warehouse.model.response.order.DataResponseOrderList;
import ru.evendot.warehouse.service.OrderService;

import java.util.UUID;


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

    @GetMapping("/order/{uuid}")
    public ResponseEntity<DataResponse> getOrder(@PathVariable UUID uuid){
        return ResponseEntity.ok(
                new DataResponse(
                        new DataResponseOrderFull(
                                orderService.getOrder(uuid)
                        )
                )
        );
    }

    @PostMapping("/order")
    public ResponseEntity<DataResponse> addOrder(@RequestBody CreateOrder orderRequest){
        return ResponseEntity.ok(
                new DataResponse(
                        new DataResponseOrder(
                                orderService.save(orderRequest)
                        )
                )
        );
    }


}
