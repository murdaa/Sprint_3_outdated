package com.example;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClient {

    private static final String ORDER_PATH = "/api/v1/orders";
    private static final String BASE_URL = "http://qa-scooter.praktikum-services.ru";

    @Step("Создать заказ")
    public Response createOrder(Order order) {
        return given().log().all()
                .baseUri(BASE_URL)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post(ORDER_PATH);
    }


    @Step("Удалить заказ")
    public boolean deleteOrder(int track) {
        return given().log().all()
                .baseUri(BASE_URL)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .put(ORDER_PATH + "/cancel?track={track}", track)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("ok");
    }

    @Step("Получить список заказов")
    public Response getListOfOrders() {
        return given().log().all()
                .baseUri(BASE_URL)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .get("/api/v1/orders");
}
}