package ru.evendot.toy_shop;

import jdk.jfr.Description;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.evendot.toy_shop.enums.StringConst;
import ru.evendot.toy_shop.model.Product;
import ru.evendot.toy_shop.utils.Specifications;

import java.util.List;

import static io.restassured.RestAssured.given;

@SpringBootTest
class RegressTests extends BaseTest {

    @Test
    @Description("Получение всех продуктов и проверка наличия у первого продукта всех полей")
    @DisplayName("Получение всех продуктов")
    public void getAllProductsTest() {
        Specifications.installSpecification(Specifications.requestSpec(StringConst.BASE_URL.toString()),
                Specifications.responseSpec(200));

        List<Product> products = given()
                .when()
                .get("/product-service/get-products")
                .then()
                .log().all()
                .extract().body().jsonPath().getList("products", Product.class);
        Product prod1 = products.getFirst();
        Assertions.assertNotNull(prod1.getId());
        Assertions.assertNotNull(prod1.getTitle());
        Assertions.assertNotNull(prod1.getArticle());
        Assertions.assertNotNull(prod1.getDescription());
        Assertions.assertNotNull(prod1.getPrice());
        Assertions.assertNotNull(prod1.getImage());
    }
}
