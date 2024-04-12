package com.sparta.tech243.store;

import com.sparta.tech243.ApiConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.fail;


public class StoreGetTests {
    public static final String BASE_URI = ApiConfig.getBaseUri();
    public static final String BASE_PATH = ApiConfig.getBasePathStore();
    @Test
    @DisplayName("Get inventory returns correct available amount")
    void getInventoryReturnsCorrectAvailableAmount() {
        Integer approved =
                given(getRequestSpec()
                        .setBasePath(BASE_PATH + "/inventory")
                        .build())
                        .when()
                        .get()
                        .then()
                        .log().all()
                        .spec(getJsonResponseWithStatus(200))
                        .extract()
                        .jsonPath().get("approved");

        MatcherAssert.assertThat(approved, Matchers.is(50));
    }

    @Test
    @DisplayName("Get inventory returns correct placed amount")
    void getInventoryReturnsCorrectPlacedAmount() {
        Integer approved =
                given(getRequestSpec()
                        .setBasePath(BASE_PATH + "/inventory")
                        .build())
                        .when()
                        .get()
                        .then()
                        .log().all()
                        .spec(getJsonResponseWithStatus(200))
                        .extract()
                        .jsonPath().get("placed");

        MatcherAssert.assertThat(approved, Matchers.is(100));
    }

    @Test
    @DisplayName("Get inventory returns correct delivered amount")
    void getInventoryReturnsCorrectDeliveredAmount() {
        Integer approved =
                given(getRequestSpec()
                        .setBasePath(BASE_PATH + "/inventory")
                        .build())
                        .when()
                        .get()
                        .then()
                        .log().all()
                        .spec(getJsonResponseWithStatus(200))
                        .extract()
                        .jsonPath().get("delivered");

        MatcherAssert.assertThat(approved, Matchers.is(50));
    }

    @Test
    @DisplayName("Get order by ID returns the correct order")
    void getOrderByIdReturnsTheCorrectOrder() {
        Integer petID =
                given(getRequestSpec()
                        .setBasePath(BASE_PATH + "/order/{order_id}")
                        .addPathParam("order_id",2)
                        .build())
                        .when()
                        .get()
                        .then()
                        .log().all()
                        .spec(getJsonResponseWithStatus(200))
                        .extract()
                        .jsonPath().get("petId");

        MatcherAssert.assertThat(petID, Matchers.is(1));
    }

    @Test
    @DisplayName("Get order by ID returns the correct quantity")
    void getOrderByIdReturnsTheCorrectQuantity() {
        Integer quantity =
                given(getRequestSpec()
                        .setBasePath(BASE_PATH + "/order/{order_id}")
                        .addPathParam("order_id",2)
                        .build())
                        .when()
                        .get()
                        .then()
                        .log().all()
                        .spec(getJsonResponseWithStatus(200))
                        .extract()
                        .jsonPath().get("quantity");

        MatcherAssert.assertThat(quantity, Matchers.is(50));
    }

    @Test
    @DisplayName("Get order by ID returns the correct ship status")
    void getOrderByIdReturnsTheCorrectShipStatus() {
        String status =
                given(getRequestSpec()
                        .setBasePath(BASE_PATH + "/order/{order_id}")
                        .addPathParam("order_id",2)
                        .build())
                        .when()
                        .get()
                        .then()
                        .log().all()
                        .spec(getJsonResponseWithStatus(200))
                        .extract()
                        .jsonPath().get("status");

        MatcherAssert.assertThat(status, Matchers.is("approved"));
    }

    @Test
    @DisplayName("Get order by ID returns the correct complete status")
    void getOrderByIdReturnsTheCorrectCompleteStatus() {
        Boolean completeStatus =
                given(getRequestSpec()
                        .setBasePath(BASE_PATH + "/order/{order_id}")
                        .addPathParam("order_id",2)
                        .build())
                        .when()
                        .get()
                        .then()
                        .log().all()
                        .spec(getJsonResponseWithStatus(200))
                        .extract()
                        .jsonPath().get("complete");

        MatcherAssert.assertThat(completeStatus, Matchers.is(true));
    }

    @Test
    @DisplayName("Get order by incorrect ID returns 404")
    void getOrderByIncorrectIDReturns404() {
        Response response =
                given(getRequestSpec()
                        .setBasePath(BASE_PATH + "/order/{order_id}")
                        .addPathParam("order_id",700)
                        .build())
                        .when()
                        .get()
                        .then()
                        .log().all()
                        .spec(getReponseStatus(404))
                        .extract()
                        .response();

        String status = response.getBody().asString();
        MatcherAssert.assertThat(status, Matchers.is("Order not found"));
    }

    private RequestSpecBuilder getRequestSpec(){
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setBasePath(BASE_PATH);
    }

    private ResponseSpecification getJsonResponseWithStatus(Integer status){
        return new ResponseSpecBuilder()
                .expectStatusCode(status)
                .expectContentType(ContentType.JSON)
                .build();
    }

    private ResponseSpecification getReponseStatus(Integer status){
        return new ResponseSpecBuilder()
                .expectStatusCode(status)
                .build();
    }
}
