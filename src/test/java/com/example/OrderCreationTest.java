package com.example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreationTest {

        private static File json;

        public OrderCreationTest(File json) {
            this.json = json;
        }


    @Parameterized.Parameters
    public static Object[][] getBodyData() {
        return new Object[][] {
                {json = new File("src/test/resources/creationOrderWithBlackColor.json")},
                {json = new File("src/test/resources/creationOrderWithGreyColor.json")},
                {json = new File("src/test/resources/creationOrderWithBothColor.json")},
                {json = new File("src/test/resources/creationOrderWithoutColor.json")}
        };
    }


    @Before
    public void setUp() {

        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";

    }


    @Test
    public void orderCreationTest() {

            Response response = given()
                    .contentType(ContentType.JSON)
                    .body(json)
                    .when()
                    .post("/api/v1/orders");

            response.then().assertThat().statusCode(201).body("track", notNullValue()).extract().response();
            int track = response.path("track");

            RestAssured.with()
                .contentType(ContentType.JSON)
                .put("/api/v1/orders/cancel?track={track}", track)
                .then()
                .statusCode(200);
    }
}
