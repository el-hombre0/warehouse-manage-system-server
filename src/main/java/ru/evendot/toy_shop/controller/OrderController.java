package ru.evendot.toy_shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.evendot.toy_shop.repository.OrdersRepository;

@RestController
@RequestMapping("/api/v1")
public class OrderController {

    @Autowired
    private OrdersRepository ordersRepo;


}
