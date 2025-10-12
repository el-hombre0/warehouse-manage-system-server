package ru.evendot.warehouse.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.evendot.warehouse.dto.OrderDTO;
import ru.evendot.warehouse.exception.ResourceNotFoundException;
import ru.evendot.warehouse.model.Order;
import ru.evendot.warehouse.model.response.DataResponse;
import ru.evendot.warehouse.service.OrderService;

import java.util.List;


@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/all")
    public ResponseEntity<DataResponse> getAllOrders() {
        List<OrderDTO> orders = orderService.getOrders();
        return ResponseEntity.ok(
                new DataResponse("Orders received successfully!", orders)
        );
    }

    @GetMapping("/order/user")
    public ResponseEntity<DataResponse> getUserOrders(@RequestParam Long userId) {
        try {
            List<OrderDTO> orders = orderService.getUserOrders(userId);
            return ResponseEntity.ok(
                    new DataResponse("Orders received successfully!", orders));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DataResponse(
                    "User with id " + userId + " not found", e.getMessage()));
        }
    }

    @GetMapping("/order")
    public ResponseEntity<DataResponse> getOrderById(@RequestParam Long id) {
        try {
            OrderDTO order = orderService.getOrder(id);
            return ResponseEntity.ok(
                    new DataResponse("Order received successfully!", order));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DataResponse(
                    "Product with id " + id + " not found", e.getMessage()));
        }
    }

    @PostMapping("/order")
    public ResponseEntity<DataResponse> createOrder(@RequestParam Long userId) {
        try {
            Order order = orderService.placeOrder(userId);
            OrderDTO orderDTO = orderService.convertToOrderDTO(order);
            return ResponseEntity.ok(new DataResponse("Order created successfully!",
                    orderDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new DataResponse("Error occurred!", e.getMessage())
            );
        }

    }


}
