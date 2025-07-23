package ru.evendot.warehouse.utils;

import ru.evendot.warehouse.api.response.product.CreateProductResponse;
import ru.evendot.warehouse.api.response.product.GetProductResponse;
import ru.evendot.warehouse.enums.StringConst;
import ru.evendot.warehouse.model.request.product.CreateProductRequest;
import ru.evendot.warehouse.model.request.product.DeleteProduct;

import static io.restassured.RestAssured.given;

public class ProductService {
    public static Long createProduct() {
        CreateProductRequest createProductRequest = new CreateProductRequest("Плюшевая лисичка", 748312L, "Рыжая лисичка, стоящая на задних лапках", 12.50, true, 0, 15);
        Specifications.installSpecification(Specifications.requestSpec(StringConst.BASE_URL.toString()), Specifications.responseSpec(200));
        CreateProductResponse response = given().body(createProductRequest)
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

    public static void deleteProduct(Long article){
        DeleteProduct product = new DeleteProduct(article);
        given().body(product).when().delete("/product-service/product").then().log().all();
    }
}
