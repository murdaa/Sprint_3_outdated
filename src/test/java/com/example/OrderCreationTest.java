package com.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

@RunWith(Parameterized.class)
public class OrderCreationTest extends OrderClient {

    private String firstName = "John";
    private String lastName = "Smith";
    private String address = "Jefferson street, 15";
    private String metroStation = "Jefferson";
    private String phone = "900-15-15";
    private int rentTime = 2;
    private String deliveryDate = "2022-05-10";
    private String comment = "no comment";
    private List<String> color;

    public OrderCreationTest(List<String> color) {
         this.color = color;
        }

    @Parameterized.Parameters
    public static Object[][] sendColor() {
        return new Object[][] {
                {Arrays.asList("BLACK","GREY")},
                {Arrays.asList("GREY")},
                {Arrays.asList("BLACK")},
                {null},
        };
    }


    @Test
    @DisplayName("Order creation with different parameter of color")
    @Description("To verify order creation with different parameter of color")
    public void orderCreationTest() {

        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, color, comment);
        int track = createOrder(order)
                .then()
                .assertThat()
                .statusCode(201)
                .body("track", Matchers.notNullValue())
                .extract()
                .path("track");
        System.out.println(track);
        deleteOrder(track);

    }
}
