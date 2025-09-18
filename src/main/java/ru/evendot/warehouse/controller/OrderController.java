package ru.evendot.warehouse.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.evendot.warehouse.exception.ResourceNotFoundException;
import ru.evendot.warehouse.model.Order;
import ru.evendot.warehouse.model.request.order.CreateOrder;
import ru.evendot.warehouse.model.response.DataResponse;
import ru.evendot.warehouse.model.response.order.DataResponseOrderFull;
import ru.evendot.warehouse.model.response.order.DataResponseOrderList;
import ru.evendot.warehouse.service.OrderService;

import java.util.List;


@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/order")
    public ResponseEntity<DataResponse> getAllOrders() {
        List<Order> orders = orderService.getOrders();
        return ResponseEntity.ok(
                new DataResponse("Orders received successfully!", orders)
        );
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<DataResponse> getOrder(@PathVariable Long id) {
        try {
            Order order = orderService.getOrder(id);
            return ResponseEntity.ok(
                    new DataResponse("Order received successfully!", order));
        }
        catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DataResponse(
                    "Product with id " + id + " not found", e.getMessage()));
        }
    }

    @PostMapping("/order")
    public ResponseEntity<DataResponse> createOrder(Long userId) {
        try{
            Order order = orderService.placeOrder(userId);
            return ResponseEntity.ok(new DataResponse("Order created successfully!", order));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataResponse("Error occurred!", e.getMessage()));
        }

    }


}
