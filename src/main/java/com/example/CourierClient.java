package com.example;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierClient {

    private static final String COURIER_PATH = "/api/v1/courier/";
    private static final String BASE_URL = "http://qa-scooter.praktikum-services.ru";

    @Step("Создать курьера")
    public Response create(Courier courier) {
        return given()
                .log().all()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(courier)
                .when()
                .post(COURIER_PATH);
    }

    @Step("Залогиниться")
    public Response loginCourier(Credentials credentials) {
        return given()
                .log().all()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(credentials)
                .when()
                .post(COURIER_PATH + "login/");
    }

    @Step("Получить id созданного курьера")
    public String getCreatedCourierId(Credentials credentials) {
        return given()
                .log().all()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(credentials)
                .when()
                .post(COURIER_PATH + "login/")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("id").toString();
    }

    @Step("Удалить курьера")
    public boolean delete(String id) {
        return given()
                .log().all()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .when()
                .delete(COURIER_PATH + id)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("ok");
    }
}
