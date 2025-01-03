package ru.evendot.toy_shop.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.evendot.toy_shop.Repositories.ProductsRepository;

@RestController
@RequestMapping("/api/v1")
public class ProductController {

    @Autowired
    private ProductsRepository productsRepo;


}
