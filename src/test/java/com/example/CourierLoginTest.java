package com.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.*;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CourierLoginTest {

    String endpointCourierLogin = "/api/v1/courier/login";

    File jsonLoginValid = new File("src/test/resources/loginCourierValid.json");
    File jsonLoginInvalidPassword = new File("src/test/resources/loginCourierInvalidPassword.json");
    File jsonLoginWithoutField = new File("src/test/resources/loginCourierWithoutLogin.json");
    File jsonLoginNotCreatedCourier = new File("src/test/resources/loginNotCreatedCourier.json");

    @Before
    public void createCourierBeforeTest() {

        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";

        String endpointCourierCreation = "/api/v1/courier";
        File jsonCreationValid = new File("src/test/resources/createCourierValid.json");

        given()
                .header("Content-type", "application/json")
                .body(jsonCreationValid)
                .post(endpointCourierCreation)
                .then()
                .statusCode(201);
    }

    @Test
    @DisplayName("Courier valid login")
    @Description("To verify courier login with valid name and password")
    public void courierLoginTest() {

        Response response = given()
                .contentType(ContentType.JSON)
                .body(jsonLoginValid)
                .when()
                .post(endpointCourierLogin);

        response.then().assertThat().statusCode(200).body("id", notNullValue());

    }


    @Test
    @DisplayName("Courier login with invalid password")
    @Description("To verify courier login with invalid password")
    public void courierLoginInvalidPasswordTest() {

        Response response = given()
                .contentType(ContentType.JSON)
                .body(jsonLoginInvalidPassword)
                .when()
                .post(endpointCourierLogin)
                .then()
                .statusCode(404)
                .extract().response();

        String expected = "Учетная запись не найдена";
        String actual = response.path("message").toString();
        Assert.assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Courier login without one required field")
    @Description("To verify courier login without one required field")
    public void courierLoginWithoutFieldTest() {

        Response response = given()
                .contentType(ContentType.JSON)
                .body(jsonLoginWithoutField)
                .when()
                .post(endpointCourierLogin)
                .then()
                .statusCode(400)
                .extract().response();

        String expected = "Недостаточно данных для входа";
        String actual = response.path("message").toString();
        Assert.assertEquals(actual, expected);

    }

    @Test
    @DisplayName("Login for courier that is not created")
    @Description("To verify non-existent courier login")
    public void courierNotCreatedLoginTest() {

        Response response = given()
                .contentType(ContentType.JSON)
                .body(jsonLoginNotCreatedCourier)
                .when()
                .post(endpointCourierLogin)
                .then()
                .statusCode(404)
                .extract().response();

        String expected = "Учетная запись не найдена";
        String actual = response.path("message").toString();
        Assert.assertEquals(actual, expected);

    }


    @After
    public void deleteCourierAfterTest() {

        Response responseLogin = given()
                .contentType(ContentType.JSON).body(jsonLoginValid)
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

