package com.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class ListOfOrderTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Test of orders' list")
    @Description("To verify a list of orders not empty")
    public void getListOfOrdersTest() {
        Response response = given()
                .header("Content-type", "application/json")
                .get("/api/v1/orders");

        response.then().assertThat().statusCode(200).and().body("orders", notNullValue());

        JsonPath jsonPathContent = response.jsonPath();
        List<Orders> allOrders = jsonPathContent.getList("orders", Orders.class);

        for(Orders order : allOrders)
        {
            System.out.println("Order ID: " + order.getId());
        }
     }

}

