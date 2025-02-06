package ru.evendot.toy_shop.utils;

import ru.evendot.toy_shop.api.response.CreateProductResponse;
import ru.evendot.toy_shop.api.response.GetProductResponse;
import ru.evendot.toy_shop.enums.StringConst;
import ru.evendot.toy_shop.model.request.CreateProduct;

import static io.restassured.RestAssured.given;

public class ProductService {
    public static Long createProduct() {
        CreateProduct createProduct = new CreateProduct("Плюшевая лисичка", 748312L, "Рыжая лисичка, стоящая на задних лапках", 12.50, "/red-fox-standing.png", true, 15);
        Specifications.installSpecification(Specifications.requestSpec(StringConst.BASE_URL.toString()), Specifications.responseSpec(200));
        CreateProductResponse response = given().body(createProduct)
                .when().post("/product-service/product")
                .then().log().all()
                .extract().body().jsonPath().getObject("data", CreateProductResponse.class);
        return response.getArticle();
    }

    public static GetProductResponse getProduct(Long article){
        return given().when().get("/product-service/product/" + article.toString())
                .then().log().all()
                .extract().body().jsonPath().getObject("data.product", GetProductResponse.class);
    }
}
