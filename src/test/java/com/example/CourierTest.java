package com.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.*;

public class CourierTest extends CourierClient {

    String login = RandomStringUtils.randomAlphanumeric(8);
    String password = RandomStringUtils.randomAlphanumeric(8);
    String firstName = RandomStringUtils.randomAlphanumeric(8);

    @Test
    @DisplayName("Courier valid creation")
    @Description("To verify courier valid creation")
    public void createCourierTest() {

        Courier courier = new Courier(login, password, firstName);
        Response response = create(courier).then().assertThat().statusCode(201).extract().response();

        String actual = response.path("ok").toString();
        String expected = "true";
        Assert.assertEquals(actual, expected);

        //получение id для последующего удаления курьера
        Credentials credentials = new Credentials(login, password);
        String id = getCreatedCourierId(credentials);
        delete(id);
    }

    @Test
    @DisplayName("Courier creation without one required field")
    @Description("To verify courier creation without one required field")
    public void createCourierWithoutOneFieldTest() {

        Courier courier = new Courier(null, password, firstName);

        Response response = create(courier).then().statusCode(400).extract().response();

        String expected = "Недостаточно данных для создания учетной записи";
        String actual = response.path("message").toString();
        Assert.assertEquals(actual, expected);

    }

    @Test
    @DisplayName("Courier creation with used login")
    @Description("To verify courier creation with already used login")
    public void createCourierTwiceSameLogin() {

        Courier courier = new Courier(login, password, firstName);
       //создание курьера для теста
        create(courier).then().statusCode(201);

        Response response = create(courier).then().statusCode(409).extract().response();

        String expected = "Этот логин уже используется. Попробуйте другой.";
        String actual = response.path("message").toString();
        Assert.assertEquals(actual, expected);

        //получение id для последующего удаления курьера
        Credentials credentials = new Credentials(login, password);
        String id = getCreatedCourierId(credentials);
        delete(id);

    }
}

