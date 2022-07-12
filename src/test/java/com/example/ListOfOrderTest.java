package com.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.notNullValue;

public class ListOfOrderTest extends OrderClient {

    @Test
    @DisplayName("Test of orders' list")
    @Description("To verify a list of orders not empty")
    public void getListOfOrdersTest() {
        getListOfOrders().then().statusCode(200).assertThat().body("orders[0].id", notNullValue());
}
}

