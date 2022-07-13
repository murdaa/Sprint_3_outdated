package com.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.*;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CourierLoginTest extends CourierClient {

    @Test
    @DisplayName("Courier valid login")
    @Description("To verify courier login with valid name and password")
    public void courierLoginTest() {

        String login = RandomStringUtils.randomAlphanumeric(8);
        String password = RandomStringUtils.randomAlphanumeric(8);
        String firstName = RandomStringUtils.randomAlphanumeric(8);

        //создание курьера для теста логина
        Courier courier = new Courier(login, password, firstName);
        create(courier);


        Credentials courierCredentials = new Credentials(login, password);
        Response response = loginCourier(courierCredentials);
        response.then().assertThat().statusCode(200).body("id", notNullValue());

        //получение id для последующего удаления курьера
        String id = response.path("id").toString();
        delete(id);

    }


    @Test
    @DisplayName("Courier login with invalid password")
    @Description("To verify courier login with invalid password")
    public void courierLoginInvalidPasswordTest() {

        String login = RandomStringUtils.randomAlphanumeric(8);
        String password = RandomStringUtils.randomAlphanumeric(8);
        String firstName = RandomStringUtils.randomAlphanumeric(8);

        //создание курьера для теста логина
        Courier courier = new Courier(login, password, firstName);
        create(courier);

        Credentials credentialsWithoutPass = new Credentials(login, null);
        Response response = loginCourier(credentialsWithoutPass);
        response.then().assertThat().statusCode(504);

        //получение id для последующего удаления курьера
        Credentials courierCredentials = new Credentials(login, password);
        String id = getCreatedCourierId(courierCredentials);
        delete(id);

    }


    @Test
    @DisplayName("Courier login without one required field")
    @Description("To verify courier login without one required field")
    public void courierLoginWithoutFieldTest() {

        String login = RandomStringUtils.randomAlphanumeric(8);
        String password = RandomStringUtils.randomAlphanumeric(8);
        String firstName = RandomStringUtils.randomAlphanumeric(8);

        //создание курьера для теста логина
        Courier courier = new Courier(login, password, firstName);
        create(courier);

        Credentials loginWithoutCredentials = new Credentials(null, password);
        Response response = loginCourier(loginWithoutCredentials);
        response.then().assertThat().statusCode(400).extract().response();

        String expected = "Недостаточно данных для входа";
        String actual = response.path("message").toString();
        Assert.assertEquals(actual, expected);

        //получение id для последующего удаления курьера
        Credentials courierCredentials = new Credentials(login, password);
        String id = getCreatedCourierId(courierCredentials);
        delete(id);

    }

    @Test
    @DisplayName("Login for courier that is not created")
    @Description("To verify non-existent courier login")
    public void courierNotCreatedLoginTest() {

        String login = RandomStringUtils.randomAlphanumeric(8);
        String password = RandomStringUtils.randomAlphanumeric(8);

        Credentials courierCredentials = new Credentials(login, password);
        Response response = loginCourier(courierCredentials);
        response.then().assertThat().statusCode(404).extract().response();

        String expected = "Учетная запись не найдена";
        String actual = response.path("message").toString();
        Assert.assertEquals(actual, expected);

    }

}



