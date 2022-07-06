package com.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import static io.restassured.RestAssured.given;


public class CourierCreationTest {

    String endpointCourierCreation = "/api/v1/courier";

    File jsonCreationValid = new File("src/test/resources/createCourierValid.json");
    File jsonCreationInvalid = new File("src/test/resources/createCourierWithoutPassword.json");

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Courier valid creation")
    @Description("To verify courier valid creation")
    public void createCourierTest() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(jsonCreationValid)
                .post(endpointCourierCreation)
                .then()
                .statusCode(201)
                .extract().response();

        
        String expected = "true";
        String actual = response.path("ok").toString();
        Assert.assertEquals(actual, expected);

    }

    @Test
    @DisplayName("Courier creation without one required field")
    @Description("To verify courier creation without one required field")
    public void createCourierWithoutOneFieldTest() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(jsonCreationInvalid)
                .post(endpointCourierCreation)
                .then()
                .statusCode(400)
                .extract().response();

        String expected = "Недостаточно данных для создания учетной записи";
        String actual = response.path("message").toString();
        Assert.assertEquals(actual, expected);

    }

    @Test
    @DisplayName("Courier creation with used login")
    @Description("To verify courier creation with already used login")
    public void createCourierTwiceSameLogin() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(jsonCreationValid)
                .post(endpointCourierCreation)
                .then()
                .statusCode(409)
                .extract().response();

        String expected = "Этот логин уже используется. Попробуйте другой.";
        String actual = response.path("message").toString();;
        Assert.assertEquals(actual, expected);

    }

    @AfterClass
    public static void deleteCourierAfterTest() {

        String endpointCourierLogin = "/api/v1/courier/login";
        File jsonLoginValid = new File("src/test/resources/loginCourierValid.json");

        Response responseLogin = given()
                .contentType(ContentType.JSON)
                .body(jsonLoginValid)
                .when()
                .post(endpointCourierLogin)
                .then()
                .extract().response();

        String id = responseLogin.path("id").toString();

        RestAssured.with()
                .contentType(ContentType.JSON)
                .delete("/api/v1/courier/{id}", id)
                .then()
                .statusCode(200);
    }
}

