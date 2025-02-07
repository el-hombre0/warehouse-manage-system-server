package ru.evendot.toy_shop;

import jdk.jfr.Description;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.evendot.toy_shop.api.response.product.CreateProductResponse;
import ru.evendot.toy_shop.api.response.product.GetProductResponse;
import ru.evendot.toy_shop.enums.StringConst;
import ru.evendot.toy_shop.model.Product;
import ru.evendot.toy_shop.model.request.product.CreateProduct;
import ru.evendot.toy_shop.model.request.product.DeleteProduct;
import ru.evendot.toy_shop.utils.ProductService;
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
                .get("/product-service/product")
                .then()
                .log().all()
                .extract().body().jsonPath().getList("data.products", Product.class);
        Product prod1 = products.getFirst();
        Assertions.assertNotNull(prod1.getId());
        Assertions.assertNotNull(prod1.getTitle());
        Assertions.assertNotNull(prod1.getArticle());
        Assertions.assertNotNull(prod1.getDescription());
        Assertions.assertNotNull(prod1.getPrice());
        Assertions.assertNotNull(prod1.getImage());
    }

    @Test
    @Description("Получение одного продукта со всеми полями")
    @DisplayName("Получение одного продукта")
    public void getProduct() {
        Long article = ProductService.createProduct();
        Specifications.installSpecification(Specifications.requestSpec(StringConst.BASE_URL.toString()), Specifications.responseSpec(200));
        GetProductResponse product = given().when().get("/product-service/product/" + article.toString())
                .then().log().all()
                .extract().body().jsonPath().getObject("data.product", GetProductResponse.class);
        Assertions.assertNotNull(product.getId());
        Assertions.assertNotNull(product.getTitle());
        Assertions.assertNotNull(product.getArticle());
        Assertions.assertNotNull(product.getDescription());
        Assertions.assertNotNull(product.getPrice());
        Assertions.assertNotNull(product.getImage());
        Assertions.assertNotNull(product.getInStock());
        Assertions.assertNotNull(product.getSale());
        Assertions.assertNotNull(product.getTimeInsert());
        Assertions.assertNotNull(product.getTimeUpdate());

        Assertions.assertEquals(article, product.getArticle());

        ProductService.deleteProduct(article);
    }

    @Test
    @Description("Добавление продукта и проверка наличия артикула")
    @DisplayName("Добавление продукта")
    public void addProduct() {
        Specifications.installSpecification(Specifications.requestSpec(StringConst.BASE_URL.toString()), Specifications.responseSpec(200));
        CreateProduct createProduct = new CreateProduct("Плюшевая лисичка", 748312L, "Рыжая лисичка, стоящая на задних лапках", 12.50, "/red-fox-standing.png", true, 15);
        CreateProductResponse response = given().body(createProduct)
                .when().post("/product-service/product")
                .then().log().all()
                .extract().body().jsonPath().getObject("data", CreateProductResponse.class);

        Assertions.assertEquals(createProduct.getArticle(), response.getArticle());

        ProductService.deleteProduct(createProduct.getArticle());

    }

    @Test
    @Description("Обновление всех параметров продукта")
    @DisplayName("Обновление продукта")
    public void updateProduct() {
        Long article = ProductService.createProduct();
        GetProductResponse gottenProduct = ProductService.getProduct(article);
        gottenProduct.setDescription("Обновленное описание");
        gottenProduct.setInStock(false);
        Specifications.installSpecification(Specifications.requestSpec(StringConst.BASE_URL.toString()), Specifications.responseSpec(200));
        CreateProductResponse product = given().body(gottenProduct).when().put("/product-service/product").then().log().all()
                .extract().jsonPath().getObject("data", CreateProductResponse.class);
        GetProductResponse newGottenProduct = ProductService.getProduct(product.getArticle());
        Assertions.assertEquals(gottenProduct.getDescription(), newGottenProduct.getDescription());
        Assertions.assertEquals(gottenProduct.getInStock(), newGottenProduct.getInStock());
        ProductService.deleteProduct(article);


    }

    @Test
    @Description("Удаление продукта после создания")
    @DisplayName("Удаление продукта")
    public void deleteProduct() {
        Long article = ProductService.createProduct();
        DeleteProduct product = new DeleteProduct(article);
        Specifications.installSpecification(Specifications.requestSpec(StringConst.BASE_URL.toString()), Specifications.responseSpec(200));
        given().body(product).when().delete("/product-service/product").then().log().all();
    }
}
